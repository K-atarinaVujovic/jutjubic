package com.jutjubiccorps.jutjubic.controller;

import com.jutjubiccorps.jutjubic.dto.CreateVideoDTO;
import com.jutjubiccorps.jutjubic.dto.VideoDTO;
import com.jutjubiccorps.jutjubic.exception.MediaIOException;
import com.jutjubiccorps.jutjubic.model.Video;
import com.jutjubiccorps.jutjubic.service.VideoService;
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
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/all")
    @PageableAsQueryParam
    public ResponseEntity<Page<VideoDTO>> getAllVideos(Pageable pageable) {
        Page<Video> videos = videoService.findAll(pageable);
        Page<VideoDTO> dtoPage = videos.map(VideoDTO::new);
        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDTO> getVideo(@PathVariable Long id) {
        Video video = videoService.findById(id);
        return new ResponseEntity<>(new VideoDTO(video), HttpStatus.OK);
    }

    @GetMapping("/thumbnail")
    public ResponseEntity<byte[]> getThumbnail(@RequestParam String path) {
        byte[] img = videoService.loadThumbnail(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Podeseno da ne bi browser probao da skine fajl
        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }

//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<VideoDTO> uploadVideo(@ModelAttribute CreateVideoDTO dto) {
//        if (dto.getVideoFile().getSize() > 200 * 1024 * 1024) {
//            throw new MediaIOException("Video exceeds maximum allowed size of 200MB");
//        }
//
//        String thumbFileName = UUID.randomUUID() + "_" + dto.getThumbnail().getOriginalFilename();
//        String videoFileName = UUID.randomUUID() + "_" + dto.getVideoFile().getOriginalFilename();
//
//        try {
//            Path thumbPath = videoService.getThumbnailPath(thumbFileName);
//            Path videoPath = videoService.getVideoPath(videoFileName);
//
//            Files.copy(dto.getThumbnail().getInputStream(), thumbPath);
//            Files.copy(dto.getVideoFile().getInputStream(), videoPath);
//
//            Video video = new Video(
//                    dto.getTitle(),
//                    dto.getDescription(),
//                    dto.getTags(),
//                    thumbPath.toString(),
//                    videoPath.toString(),
//                    dto.getLocation()
//            );
//
//            Video saved = videoService.save(video);
//            return new ResponseEntity<>(new VideoDTO(saved), HttpStatus.CREATED);
//
//        } catch (Exception e) {
//            throw new MediaIOException("Failed to save uploaded files");
//        }
//    }
//

    // ModelAttribute - nije ogranicen na JSON, daje file upload
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoDTO> uploadVideo(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam List<String> tags,
            @RequestPart MultipartFile thumbnail,
            @RequestPart MultipartFile videoFile,
            @RequestParam(required = false) String location
    ) {
        // Ogranicava na 200mb
        if (videoFile.getSize() > 200 * 1024 * 1024) {
            throw new MediaIOException("Video exceeds maximum allowed size of 200MB");
        }

        //Spreci kolizije
        String thumbFileName = UUID.randomUUID() + "_" + thumbnail.getOriginalFilename();
        String videoFileName = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();

        try {
            Path thumbPath = videoService.getThumbnailPath(thumbFileName);
            Path videoPath = videoService.getVideoPath(videoFileName);

            Files.copy(thumbnail.getInputStream(), thumbPath);
            Files.copy(videoFile.getInputStream(), videoPath);

            Video video = new Video(
                    title,
                    description,
                    tags,
                    thumbPath.toString(),
                    videoPath.toString(),
                    location
            );

            Video saved = videoService.save(video);
            return new ResponseEntity<>(new VideoDTO(saved), HttpStatus.CREATED);

        } catch (Exception e) {
            throw new MediaIOException("Failed to save uploaded files");
        }
    }
}
