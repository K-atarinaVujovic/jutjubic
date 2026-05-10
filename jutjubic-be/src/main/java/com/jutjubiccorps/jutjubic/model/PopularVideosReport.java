package com.jutjubiccorps.jutjubic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "POPULAR_VIDEOS_REPORTS")
@NoArgsConstructor
public class PopularVideosReport {
    public PopularVideosReport(
            Instant runAt,
            Video video,
            double popularityScore,
            int rank
    ){
        this.runAt = runAt;
        this.video = video;
        this.popularityScore = popularityScore;
        this.rank = rank;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "run_at", nullable = false)
    private Instant runAt;

    @ManyToOne
    @JoinColumn(name = "video_id")
    @Getter
    private Video video;

    @Column(name = "popularity_score", nullable = false)
    private double popularityScore;

    @Column(name = "rank", nullable = false)
    private int rank; //1, 2, 3
}
