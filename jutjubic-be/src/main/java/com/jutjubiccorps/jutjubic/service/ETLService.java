package com.jutjubiccorps.jutjubic.service;

import com.jutjubiccorps.jutjubic.model.PopularVideosReport;
import com.jutjubiccorps.jutjubic.model.Video;
import com.jutjubiccorps.jutjubic.model.VideoView;
import com.jutjubiccorps.jutjubic.repository.PopularVideosReportRepository;
import com.jutjubiccorps.jutjubic.repository.VideoViewRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ETLService {
    private final VideoViewRepository videoViewRepository;
    private final PopularVideosReportRepository popularVideosReportRepository;
    private final EntityManager entityManager;

    @Transactional
    @Scheduled(cron = "0 51 10 * * *")
    public void runEtlPipeline() {
        Instant now = Instant.now();
        Instant sevenDaysAgo = now.minus(7, ChronoUnit.DAYS);

        // Extract
        List<VideoView> videoViews = videoViewRepository.findAllByViewedAtAfter(sevenDaysAgo);

        // Transform
        Map<Long, Double> scores = calculatePopularityScores(now, videoViews);
        Map<Long, Double> top3 = scores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));


        // Load
        int rank = 1;
        for(Map.Entry<Long, Double> entry : top3.entrySet()) {
            Long videoId = entry.getKey();
            Double score = entry.getValue();
            Video video = entityManager.getReference(Video.class, videoId);

            PopularVideosReport report = new PopularVideosReport(now, video, score, rank);
            popularVideosReportRepository.save(report);
            rank++;
        }
    }

    private Map<Long, Double> calculatePopularityScores(Instant now, List<VideoView> videoViews) {
        // Get views by video
        Map<Long, List<VideoView>> viewsByVideo = new HashMap<>();
        for (VideoView view : videoViews) {
            Long videoId = view.getVideo().getId();
            viewsByVideo.computeIfAbsent(videoId, k -> new ArrayList<>()).add(view);
        }

        // Calculate scores
        Map<Long, Double> scores = new HashMap<>();
        for (Map.Entry<Long, List<VideoView>> entry : viewsByVideo.entrySet()) {
            Long videoId = entry.getKey();
            List<VideoView> views = entry.getValue();
            int[] viewCountsPerDay = new int[7];
            for (VideoView view : views) {
                int daysAgo = (int) ChronoUnit.DAYS.between(view.getViewedAt(), now);
                viewCountsPerDay[daysAgo]++;
            }
            double popularityScore = 0;
            for (int i = 0; i < viewCountsPerDay.length; i++) {
                popularityScore += viewCountsPerDay[i] * (7 - i + 1);
            }
            scores.put(videoId, popularityScore);
        }

        return scores;
    }
}


