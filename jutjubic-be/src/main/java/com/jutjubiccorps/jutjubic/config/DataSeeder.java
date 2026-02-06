package com.jutjubiccorps.jutjubic.config;

import com.jutjubiccorps.jutjubic.model.User;
import com.jutjubiccorps.jutjubic.model.Video;
import com.jutjubiccorps.jutjubic.service.UserService;
import com.jutjubiccorps.jutjubic.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
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
    public void run(ApplicationArguments args){
        userService.registerUser(new User("Fato", "Zirosrag", "fato134@yahoo.com", "facini", "fata12345", "Bulevar Vladike Stepe 123"));
        userService.registerUser(new User("Pera", "Peric", "pera@yahoo.com", "pera", "pera", "Perina adresa 3"));

        seedVideos();
    }

    private void seedVideos(){
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

        videoService.save(video1);
        videoService.save(video2);
    }
}
