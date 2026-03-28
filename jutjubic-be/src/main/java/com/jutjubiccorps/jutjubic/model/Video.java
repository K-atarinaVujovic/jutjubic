package com.jutjubiccorps.jutjubic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        this.scheduledAt = null;
    }

    public Video(
            String title,
            String description,
            List<String> tags,
            String thumbnailUrl,
            String videoUrl,
            String location,
            LocalDateTime scheduledAt
    ) {
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.location = location;
        this.scheduledAt = scheduledAt;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
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

    @Column(name = "viewCount", nullable = false)
    @Getter
    private int viewCount = 0;

    @Column(name = "scheduledAt", nullable = true)
    @Getter @Setter
    private LocalDateTime scheduledAt;

    public synchronized void incrementViewCount() {
        this.viewCount++;
    }

    // Podesi datum pri pravljenju
    @PrePersist
    void onCreate() {
        if (this.dateCreated == null) {
            this.dateCreated = Instant.now();
        }
    }

    @Transient
    public boolean isLive(){
        if (scheduledAt == null) return false;
        LocalDateTime now = LocalDateTime.now();
        boolean startedStreaming = now.isAfter(scheduledAt);
        boolean finishedStreaming = Duration.between(scheduledAt, now).getSeconds() >= durationSeconds;
        return startedStreaming && !finishedStreaming;
    }

    @Transient
    public boolean isForRegularViewing(){
        if (scheduledAt == null) return true;
        boolean finishedStreaming = Duration.between(scheduledAt, LocalDateTime.now()).getSeconds() >= durationSeconds;
        return finishedStreaming;
    }

    @Column(nullable=false)
    @Getter @Setter
    double durationSeconds;

    @Column(nullable = true)
    @Getter @Setter
    private String location;
}
