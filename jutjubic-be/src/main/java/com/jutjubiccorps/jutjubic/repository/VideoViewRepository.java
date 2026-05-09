package com.jutjubiccorps.jutjubic.repository;

import com.jutjubiccorps.jutjubic.model.VideoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface VideoViewRepository extends JpaRepository<VideoView, Long> {
    List<VideoView> findAllByViewedAtAfter(Instant since);
    List<VideoView> findAllByVideoId(Long videoId);
    @Query("SELECT v FROM VideoView v WHERE v.viewedAt > :since AND v.video.id = :videoId")
    List<VideoView> findAllByVideoIdSince(@Param("since") Instant since, @Param("videoId") Long videoId);
    VideoView findOneByVideoId(long videoId);
    long countByVideoId(long videoId);
}
