package com.jutjubiccorps.jutjubic.controller;

import com.jutjubiccorps.jutjubic.dto.CreateVideoDTO;
import com.jutjubiccorps.jutjubic.dto.VideoDTO;
import com.jutjubiccorps.jutjubic.exception.MediaIOException;
import com.jutjubiccorps.jutjubic.exception.NotFoundException;
import com.jutjubiccorps.jutjubic.model.Comment;
import com.jutjubiccorps.jutjubic.model.Like;
import com.jutjubiccorps.jutjubic.model.Video;
import com.jutjubiccorps.jutjubic.service.UserService;
import com.jutjubiccorps.jutjubic.service.VideoInteractionService;
import com.jutjubiccorps.jutjubic.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080" })
@RequestMapping("api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final VideoInteractionService videoInteractionService;
    private final UserService userService;

//    @GetMapping("/all")
//    @PageableAsQueryParam
//    public ResponseEntity<Page<VideoDTO>> getAllVideos(Pageable pageable) {
//        Page<Video> videos = videoService.findAll(pageable);
//        Page<VideoDTO> dtoPage = videos.map(VideoDTO::new);
//        return ResponseEntity.ok(dtoPage);
//    }

    @GetMapping("/all-sorted")
    public ResponseEntity<List<VideoDTO>> getAllVideosSorted() {
        List<Video> videos = videoService.findAllSortedByDate();
        List<VideoDTO> dtos = videos.stream().map(VideoDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDTO> getVideo(@PathVariable Long id) throws IOException {
        // view video
        videoService.incrementViewCount(id);
        Video video = videoService.findById(id);
        return ResponseEntity.ok(new VideoDTO(video));
    }

    @GetMapping("/play")
    public ResponseEntity<byte[]> getVideoFile(@RequestParam Long id) {
        byte[] videoBytes = videoService.loadVideo(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("video/mp4"));
        return new ResponseEntity<>(videoBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}/hls/{filename}")
    public ResponseEntity<Resource> serveHls (@PathVariable Long id, @PathVariable String filename) throws IOException {
        Video video = videoService.findById(id);
        // Prevent forwarding
        if (video.isLive() && filename.endsWith(".ts")) {
            int requestedChunk = Integer.parseInt(filename.replaceAll("[^0-9]", ""));
//            long offsetSeconds = Duration.between(video.getScheduledAt(), LocalDateTime.now()).getSeconds();
//            int currentChunk = (int)(offsetSeconds / 6); // 6 = hls_time
            int currentChunk = videoService.getCurrentChunk(video);
//            System.out.println("=======\n======\n======\n======Requested: " + requestedChunk + ", Current: " + currentChunk + ", Offset: " + Duration.between(video.getScheduledAt(), LocalDateTime.now()).getSeconds() + "s");

            if (requestedChunk > currentChunk) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        Path file = Paths.get("uploads/hls/" + id + "/" + filename);
        Resource resource = new FileSystemResource(file);

        String contentType = filename.endsWith(".m3u8")
                ? "application/vnd.apple.mpegurl"
                : "video/MP2T";

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    @GetMapping("/thumbnail")
    public ResponseEntity<byte[]> getThumbnail(@RequestParam Long id) {
        byte[] img = videoService.loadThumbnail(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoDTO> uploadVideo(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam List<String> tags,
            @RequestPart MultipartFile thumbnail,
            @RequestPart MultipartFile videoFile,
            @RequestParam(required = false) String location
    ) {
        if (videoFile.getSize() > 200 * 1024 * 1024) {
            throw new MediaIOException("Video exceeds maximum allowed size of 200MB");
        }

        String thumbFileName = UUID.randomUUID() + "_" + thumbnail.getOriginalFilename();
        String videoFileName = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();

        try {
            Path thumbPath = videoService.getThumbnailPath(thumbFileName);
            Path videoPath = videoService.getVideoPath(videoFileName);

            Files.copy(thumbnail.getInputStream(), thumbPath);
            Files.copy(videoFile.getInputStream(), videoPath);

            Video video = new Video(title, description, tags, thumbPath.toString(), videoPath.toString(), location);
            Video saved = videoService.save(video);
            return new ResponseEntity<>(new VideoDTO(saved), HttpStatus.CREATED);

        } catch (Exception e) {
            throw new MediaIOException("Failed to save uploaded files");
        }
    }

    @PostMapping("/{videoId}/comments")
    public Comment postComment(@PathVariable Long videoId,
                               @RequestParam Long userId,
                               @RequestParam String text) {
        return videoInteractionService.addComment(videoId, userId, text);
    }

    @GetMapping("/{videoId}/comments")
    public List<Comment> getComments(@PathVariable Long videoId) {
        return videoInteractionService.getComments(videoId);
    }

    @PostMapping("/{videoId}/likes")
    public ResponseEntity<String> postLike(@PathVariable Long videoId,
                                           @RequestParam Long userId) {
        Like like = videoInteractionService.addLike(videoId, userId);
        if (like == null) return ResponseEntity.badRequest().body("Already liked");
        return ResponseEntity.ok("Liked");
    }

    @DeleteMapping("/{videoId}/likes")
    public ResponseEntity<String> removeLike(@PathVariable Long videoId, @RequestParam Long userId){
        try{
            videoInteractionService.removeLike(videoId, userId);
            return ResponseEntity.ok("Like removed");
        }
        catch(NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("/{videoId}/likes/{userId}")
    public ResponseEntity<Boolean> hasUserLiked(@PathVariable Long videoId, @PathVariable Long userId){
        return ResponseEntity.ok(videoInteractionService.hasUserLiked(videoId, userId));
    }

    @GetMapping("/{videoId}/likes")
    public int getLikes(@PathVariable Long videoId) {
        return videoInteractionService.getLikes(videoId);
    }
}
