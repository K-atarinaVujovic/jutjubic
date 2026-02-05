package com.jutjubiccorps.jutjubic.dto;

import com.jutjubiccorps.jutjubic.model.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO {

    public VideoDTO(Video video) {
        this(
                video.getId(),
                video.getTitle(),
                video.getDescription(),
                video.getTags(),
                video.getThumbnailUrl(),
                video.getVideoUrl(),
                video.getDateCreated(),
                video.getLocation()
        );
    }

    @Getter
    private Long id;

    @Getter
    private String title;

    @Getter
    private String description;

    @Getter
    private List<String> tags;

    @Getter
    private String thumbnailUrl;

    @Getter
    private String videoUrl;

    @Getter
    private Instant dateCreated;

    @Getter
    private String location;
}
