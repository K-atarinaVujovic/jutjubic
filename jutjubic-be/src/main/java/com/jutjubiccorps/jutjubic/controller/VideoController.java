package com.jutjubiccorps.jutjubic.controller;

import com.jutjubiccorps.jutjubic.dto.CreateVideoDTO;
import com.jutjubiccorps.jutjubic.dto.VideoDTO;
import com.jutjubiccorps.jutjubic.exception.MediaIOException;
import com.jutjubiccorps.jutjubic.model.Comment;
import com.jutjubiccorps.jutjubic.model.Like;
import com.jutjubiccorps.jutjubic.model.Video;
import com.jutjubiccorps.jutjubic.service.UserService;
import com.jutjubiccorps.jutjubic.service.VideoInteractionService;
import com.jutjubiccorps.jutjubic.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
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

    @GetMapping("/all")
    @PageableAsQueryParam
    public ResponseEntity<Page<VideoDTO>> getAllVideos(Pageable pageable) {
        Page<Video> videos = videoService.findAll(pageable);
        Page<VideoDTO> dtoPage = videos.map(VideoDTO::new);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/all-sorted")
    public ResponseEntity<List<VideoDTO>> getAllVideosSorted() {
        List<Video> videos = videoService.findAllSortedByDate();
        List<VideoDTO> dtos = videos.stream().map(VideoDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDTO> getVideo(@PathVariable Long id) {
        // view video
        videoService.incrementViewCount(id);
        Video video = videoService.findById(id);
        return ResponseEntity.ok(new VideoDTO(video));
    }

    @GetMapping("/thumbnail")
    public ResponseEntity<byte[]> getThumbnail(@RequestParam String path) {
        byte[] img = videoService.loadThumbnail(path);
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

    @GetMapping("/{videoId}/likes")
    public int getLikes(@PathVariable Long videoId) {
        return videoInteractionService.getLikes(videoId);
    }
}
