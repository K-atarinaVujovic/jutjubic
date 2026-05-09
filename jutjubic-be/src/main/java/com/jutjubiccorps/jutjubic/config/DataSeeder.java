package com.jutjubiccorps.jutjubic.config;

import com.jutjubiccorps.jutjubic.model.User;
import com.jutjubiccorps.jutjubic.model.Video;
import com.jutjubiccorps.jutjubic.model.VideoView;
import com.jutjubiccorps.jutjubic.repository.VideoViewRepository;
import com.jutjubiccorps.jutjubic.service.UserService;
import com.jutjubiccorps.jutjubic.service.VideoInteractionService;
import com.jutjubiccorps.jutjubic.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {
    private final UserService userService;
    private final VideoService videoService;
    private final VideoViewRepository videoViewRepository;

    // fato@yahoo.com fato
    // pera@yahoo.com pera
    @Override
    public void run(ApplicationArguments args) throws IOException{
        User f = userService.registerUser(new User("Fato", "Zirosrag", "fato@yahoo.com", "facini", "fato", "Bulevar Vladike Stepe 123"));
        User p = userService.registerUser(new User("Pera", "Peric", "pera@yahoo.com", "pera", "pera", "Perina adresa 3"));
        userService.activateUser(f);
        userService.activateUser(p);
        seedVideos();
    }

    private void seedVideos() throws IOException {
        Instant now = Instant.now();
        String thumbnailUrl = "uploads/thumbnails/";
        String videoUrl = "uploads/videos/";

        Video video1 = new Video(
                "cat stare 1",
                "A compilation of funny cat videos",
                List.of("cats", "funny", "compilation"),
                thumbnailUrl + "thumbnail1.png",
                videoUrl + "video1.mp4",
                "Home",
                LocalDateTime.now().minusDays(15)
        );

        Video video2 = new Video(
                "cat core 2",
                "A compilation of funny cat videos",
                List.of("haha", "a", "mm"),
                thumbnailUrl + "thumbnail2.png",
                videoUrl + "video2.mp4",
                "Home",
                LocalDateTime.now().minusDays(15)
        );
        video2.setDateCreated(now.minus(1, ChronoUnit.HOURS));

        // scheduled videos
        Video video3 = new Video(
                "Ultimate cat stare 3",
                "Ultimate compilation",
                List.of("cats", "funny", "compilation"),
                thumbnailUrl + "thumbnail1.png",
                videoUrl + "video1.mp4",
                "Ultimate Home",
                LocalDateTime.now().minusMinutes(10)
        );

        Video video4 = new Video(
                "ultimate cat core 4",
                "ultimate compilation of funny cat videos",
                List.of("ok"),
                thumbnailUrl + "thumbnail2.png",
                videoUrl + "video2.mp4",
                "Ultimate home",
                LocalDateTime.now().plusMinutes(2)
        );

        // more videos for etl
        Video video5 = new Video(
                "cat stare 5",
                "A compilation of funny cat videos",
                List.of("cats", "funny", "compilation"),
                thumbnailUrl + "thumbnail1.png",
                videoUrl + "video1.mp4",
                "Home",
                LocalDateTime.now().minusDays(15)
        );

        Video video6 = new Video(
                "cat stare 6",
                "A compilation of funny cat videos",
                List.of("cats", "funny", "compilation"),
                thumbnailUrl + "thumbnail1.png",
                videoUrl + "video1.mp4",
                "Home",
                LocalDateTime.now().minusDays(15)
        );

        Video video7 = new Video(
                "cat stare 7",
                "A compilation of funny cat videos",
                List.of("cats", "funny", "compilation"),
                thumbnailUrl + "thumbnail1.png",
                videoUrl + "video1.mp4",
                "Home",
                LocalDateTime.now().minusDays(15)
        );

        Video savedVideo1 = videoService.save(video1);
        Video savedVideo2 = videoService.save(video2);
        Video savedVideo3 = videoService.save(video3);
        Video savedVideo4 = videoService.save(video4);
        Video savedVideo5 = videoService.save(video5);
        Video savedVideo6 = videoService.save(video6);
        Video savedVideo7 = videoService.save(video7);

        // Seed views
        // Video 1
        seedViews(savedVideo1, 50, now.minus(1, ChronoUnit.DAYS));
        seedViews(savedVideo1, 5, now.minus(3, ChronoUnit.DAYS));
        seedViews(savedVideo1, 5, now.minus(6, ChronoUnit.DAYS));

        // Video 2
        seedViews(savedVideo2, 60, now.minus(6, ChronoUnit.DAYS));
        seedViews(savedVideo2, 1, now.minus(1, ChronoUnit.DAYS));

        // Others
        for(Video video : List.of(savedVideo5, savedVideo6, savedVideo7)){
            seedViews(video, 5, now.minus(1, ChronoUnit.DAYS));
        }
    }

    private void seedViews(Video video, int count, Instant viewedAt) {
        for(int i = 0; i < count; i++){
            VideoView view = new VideoView(video);
            view.setViewedAt(viewedAt);
            videoViewRepository.save(view);
        }
    }
}
