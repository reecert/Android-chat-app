package com.example.chatserver.repository;

import com.example.chatserver.model.ModerationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModerationReportRepository extends JpaRepository<ModerationReport, Long> {
    List<ModerationReport> findByStatus(ModerationReport.ReportStatus status);
}
