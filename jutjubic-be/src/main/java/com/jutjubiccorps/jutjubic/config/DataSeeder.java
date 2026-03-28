package com.jutjubiccorps.jutjubic.config;

import com.jutjubiccorps.jutjubic.model.User;
import com.jutjubiccorps.jutjubic.model.Video;
import com.jutjubiccorps.jutjubic.service.UserService;
import com.jutjubiccorps.jutjubic.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {
    private final UserService userService;
    private final VideoService videoService;

    // fato134@yahoo.com fata12345
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
        String thumbnailUrl = "uploads\\thumbnails\\";
        String videoUrl = "uploads\\videos\\";

        Video video1 = new Video(
                "cat stare",
                "A compilation of funny cat videos",
                List.of("cats", "funny", "compilation"),
                thumbnailUrl + "thumbnail1.png",
                videoUrl + "video1.mp4",
                "Home"
        );

        Video video2 = new Video(
                "cat core",
                "A compilation of funny cat videos",
                List.of("haha", "a", "mm"),
                thumbnailUrl + "thumbnail2.png",
                videoUrl + "video2.mp4",
                "Home"
        );
        video2.setDateCreated(now.minus(1, ChronoUnit.HOURS));

        // scheduled videos
        Video video3 = new Video(
                "Ultimate cat stare",
                "Ultimate compilation",
                List.of("cats", "funny", "compilation"),
                thumbnailUrl + "thumbnail1.png",
                videoUrl + "video1.mp4",
                "Ultimate Home",
                LocalDateTime.now().plusMinutes(10)
        );

        Video video4 = new Video(
                "ultimate cat core",
                "ultimate compilation of funny cat videos",
                List.of("ok"),
                thumbnailUrl + "thumbnail2.png",
                videoUrl + "video2.mp4",
                "Ultimate home",
                LocalDateTime.now().plusMinutes(3)
        );

        videoService.save(video1);
        videoService.save(video2);
        videoService.save(video3);
        videoService.save(video4);
    }
}
