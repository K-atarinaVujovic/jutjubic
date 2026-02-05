package com.jutjubiccorps.jutjubic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "VIDEOS")
public class Video {
    public Video() {
    }

    public Video(
            String title,
            String description,
            List<String> tags,
            String thumbnailUrl,
            String videoUrl,
            String location
    ) {
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.location = location;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "title", nullable = false)
    @Getter @Setter
    private String title;

    @Column(name = "description", nullable = false)
    @Getter @Setter
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "video_tags", joinColumns = @JoinColumn(name = "video_id"))
    @Getter @Setter
    private List<String> tags;

    @Column(name = "thumbnailUrl", nullable = false)
    @Getter @Setter
    private String thumbnailUrl;

    @Column(name = "videoUrl", nullable = false)
    @Getter @Setter
    private String videoUrl;

    @Column(name = "dateCreated", updatable = false, nullable = false)
    @Getter
    private Instant dateCreated;

    // Podesi datum pri pravljenju
    @PrePersist
    void onCreate() {
        this.dateCreated = Instant.now();
    }

    @Column(nullable = true)
    @Getter @Setter
    private String location;
}
