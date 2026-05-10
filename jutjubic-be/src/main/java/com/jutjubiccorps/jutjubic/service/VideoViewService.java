package com.jutjubiccorps.jutjubic.service;

import com.jutjubiccorps.jutjubic.repository.VideoViewRepository;
import org.springframework.stereotype.Service;

@Service
public class VideoViewService {
    private final VideoViewRepository videoViewRepository;

    public VideoViewService(
            VideoViewRepository videoViewRepository
    ){
        this.videoViewRepository = videoViewRepository;
    }

    public long countByVideoId(Long videoId){
        return videoViewRepository.countByVideoId(videoId);
    }

}
