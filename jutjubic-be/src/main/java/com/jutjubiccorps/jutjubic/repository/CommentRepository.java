package com.jutjubiccorps.jutjubic.repository;

import com.jutjubiccorps.jutjubic.model.Comment;
import com.jutjubiccorps.jutjubic.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByVideoOrderByDateCreatedDesc(Video video);
}

