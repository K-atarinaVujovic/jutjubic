package com.jutjubiccorps.jutjubic.service;

import com.jutjubiccorps.jutjubic.model.Comment;
import com.jutjubiccorps.jutjubic.model.Like;
import com.jutjubiccorps.jutjubic.model.User;
import com.jutjubiccorps.jutjubic.model.Video;
import com.jutjubiccorps.jutjubic.repository.CommentRepository;
import com.jutjubiccorps.jutjubic.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoInteractionService {
    private final CommentRepository commentRepo;
    private final LikeRepository likeRepo;
    private final VideoService videoService;
    private final UserService userService;

    public Comment addComment(Long videoId, Long userId, String text) {
        Video video = videoService.findById(videoId);
        User user = userService.findById(userId);

        Comment comment = new Comment();
        comment.setVideo(video);
        comment.setUser(user);
        comment.setText(text);

        return commentRepo.save(comment);
    }

    public Like addLike(Long videoId, Long userId) {
        Video video = videoService.findById(videoId);
        User user = userService.findById(userId);

        if (likeRepo.existsByVideoAndUser(video, user)) return null;

        Like like = new Like();
        like.setVideo(video);
        like.setUser(user);

        return likeRepo.save(like);
    }

    public int getLikes(Long videoId) {
        Video video = videoService.findById(videoId);
        return likeRepo.countByVideo(video);
    }

    public List<Comment> getComments(Long videoId) {
        Video video = videoService.findById(videoId);
        return commentRepo.findByVideoOrderByDateCreatedDesc(video);
    }
}
