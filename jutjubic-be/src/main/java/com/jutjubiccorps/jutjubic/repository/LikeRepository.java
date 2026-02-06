package com.jutjubiccorps.jutjubic.repository;

import com.jutjubiccorps.jutjubic.model.Like;
import com.jutjubiccorps.jutjubic.model.User;
import com.jutjubiccorps.jutjubic.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    int countByVideo(Video video);
    boolean existsByVideoAndUser(Video video, User user);
}