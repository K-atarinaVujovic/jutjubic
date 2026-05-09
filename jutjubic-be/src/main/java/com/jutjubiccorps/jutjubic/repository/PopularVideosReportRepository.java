package com.jutjubiccorps.jutjubic.repository;

import com.jutjubiccorps.jutjubic.model.PopularVideosReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PopularVideosReportRepository extends JpaRepository<PopularVideosReport, Long> {
    @Query("SELECT p FROM PopularVideosReport p WHERE p.runAt = (SELECT MAX(r.runAt) FROM PopularVideosReport r) ORDER BY p.rank ASC")
    List<PopularVideosReport> findLatestReport();
}
