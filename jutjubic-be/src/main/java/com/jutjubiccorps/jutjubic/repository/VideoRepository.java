package com.jutjubiccorps.jutjubic.repository;

import com.jutjubiccorps.jutjubic.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {

    Video findOneById(Long id);

    @Query("SELECT v FROM Video v WHERE v.id = :id AND (v.scheduledAt IS NULL OR v.scheduledAt < CURRENT_TIMESTAMP)")
    Optional<Video> findVisibleById(@Param("id") Long id);

    boolean existsById(Long id);

    Page<Video> findAllByOrderByDateCreatedDesc(Pageable pageable);

    Page<Video> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT v FROM Video v WHERE v.scheduledAt IS NULL OR v.scheduledAt < CURRENT_TIMESTAMP")
    List<Video> findAllVisible();

    @Query("SELECT v FROM Video v WHERE v.scheduledAt IS NULL OR v.scheduledAt < CURRENT_TIMESTAMP")
    List<Video> findAllVisibleSorted(Sort sort);
}
