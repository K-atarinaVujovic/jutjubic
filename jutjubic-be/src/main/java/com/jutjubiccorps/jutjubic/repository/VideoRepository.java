package com.jutjubiccorps.jutjubic.repository;

import com.jutjubiccorps.jutjubic.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {

    Video findOneById(Long id);

    boolean existsById(Long id);

    Page<Video> findAllByOrderByDateCreatedDesc(Pageable pageable);

    Page<Video> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
