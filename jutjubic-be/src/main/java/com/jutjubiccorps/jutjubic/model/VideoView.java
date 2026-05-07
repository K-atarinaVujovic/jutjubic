package com.jutjubiccorps.jutjubic.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "VIDEO_VIEWS")
public class VideoView {
    public VideoView() {}

    public VideoView(Video video) {
        this.video = video;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @Column(name = "viewed_at", nullable = false)
    private Instant viewedAt;

    @PrePersist
    void onCreate() {
        if (this.viewedAt == null) this.viewedAt = Instant.now();
    }
}
