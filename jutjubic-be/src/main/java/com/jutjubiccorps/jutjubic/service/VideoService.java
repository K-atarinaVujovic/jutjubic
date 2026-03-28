package com.jutjubiccorps.jutjubic.service;

import com.jutjubiccorps.jutjubic.exception.MediaIOException;
import com.jutjubiccorps.jutjubic.exception.NotFoundException;
import com.jutjubiccorps.jutjubic.model.Video;
import com.jutjubiccorps.jutjubic.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    @Value("${upload-dir}")
    private String uploadDir;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    //region CRUD
    public Video save(Video video) throws IOException{
        double duration = extractDuration(video.getVideoUrl());
        video.setDurationSeconds(duration);
        return videoRepository.save(video);
    }

    private double extractDuration(String videoPath) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                "ffprobe", "-v", "error",
                "-show_entries", "format=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                videoPath
        );
        Process process = pb.start();
        String output = new String(process.getInputStream().readAllBytes());
        return Double.parseDouble(output.trim());
    }

    public void remove(Long id) {
        if (!videoRepository.existsById(id)) {
            throw new NotFoundException("Video " + id + " not found");
        }
        videoRepository.deleteById(id);
    }

    public Video findById(Long id) {
        return videoRepository.findVisibleById(id)
                .orElseThrow(() -> new NotFoundException("Video " + id + " not found"));
    }

    public List<Video> findAll() {

//        return videoRepository.findAll();
        return videoRepository.findAllVisible();
    }


//    public Page<Video> findAll(Pageable pageable) {
//        return videoRepository.findAllByOrderByDateCreatedDesc(pageable);
//    }

//    public Page<Video> searchByTitle(String title, Pageable pageable) {
//        return videoRepository.findByTitleContainingIgnoreCase(title, pageable);
//    }

    @Cacheable("thumbnails")
    public byte[] loadThumbnail(Long id)  {
        String path = findById(id).getThumbnailUrl();
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            throw new MediaIOException("Failed to load thumbnail: " + path);
        }
    }

    // Helper
    public Path getVideoPath(String fileName) {
        try {
            Path path = Path.of(uploadDir, "videos", fileName);
            Files.createDirectories(path.getParent());
            return path;
        } catch (IOException e) {
            throw new MediaIOException("Failed to create video path for: " + fileName);
        }
    }

    public Path getThumbnailPath(String fileName) {
        try {
            Path path = Path.of(uploadDir, "thumbnails", fileName);
            Files.createDirectories(path.getParent());
            return path;
        } catch (IOException e) {
            throw new MediaIOException("Failed to create thumbnail path for: " + fileName);
        }
    }

    public List<Video> findAllSortedByDate() {
        List<Video> videos = videoRepository.findAllVisibleSorted(Sort.by(Sort.Direction.DESC, "dateCreated"));
        return videos;
    }

    public byte[] loadVideo(Long videoId) {
        String path = findById(videoId).getVideoUrl();
        try {
            return Files.readAllBytes(Path.of(path)); // read full file
        } catch (IOException e) {
            throw new MediaIOException("Failed to load video: " + path);
        }
    }

    public void incrementViewCount(Long videoId) throws IOException{
        Video video = findById(videoId);
        video.incrementViewCount();
        save(video);
    }
}
