package com.jutjubiccorps.jutjubic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @Getter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    @Getter
    private Video video;

    @Column(name = "viewed_at", nullable = false)
    @Getter @Setter
    private Instant viewedAt;

    @PrePersist
    void onCreate() {
        if (this.viewedAt == null) this.viewedAt = Instant.now();
    }
}
