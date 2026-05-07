package com.jutjubiccorps.jutjubic.repository;

import com.jutjubiccorps.jutjubic.model.VideoView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface VideoViewRepository extends JpaRepository<VideoView, Long> {
    List<VideoView> findAllByViewedAtAfter(Instant since);
    List<VideoView> findAllByVideoId(Long videoId);
    VideoView findOneByVideoId(long videoId);
    long countByVideoId(long videoId);
}
