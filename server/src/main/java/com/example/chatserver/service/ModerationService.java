package com.example.chatserver.service;

import com.example.chatserver.model.ModerationReport;
import com.example.chatserver.repository.ModerationReportRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class ModerationService {

    private static final Logger log = LoggerFactory.getLogger(ModerationService.class);

    private final ModerationReportRepository repository;

    public ModerationService(ModerationReportRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ModerationReport createReport(String chatId, String messageId, String reporterUid, String reason) {
        ModerationReport report = new ModerationReport();
        report.setChatId(chatId);
        report.setMessageId(messageId);
        report.setReporterUid(reporterUid);
        report.setReason(reason);
        report.setStatus(ModerationReport.ReportStatus.OPEN);
        report.setCreatedAt(Instant.now());
        ModerationReport saved = repository.save(report);
        log.info("Created moderation report {} for message {} in chat {}", saved.getReportId(), messageId, chatId);
        return saved;
    }

    @Transactional
    public void resolveReport(Long reportId) {
        ModerationReport report = repository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with id: " + reportId));
        report.setStatus(ModerationReport.ReportStatus.RESOLVED);
        report.setResolvedAt(Instant.now());
        repository.save(report);
        log.info("Resolved moderation report {}", reportId);
    }

    public List<ModerationReport> getReports(ModerationReport.ReportStatus status) {
        return repository.findByStatus(status);
    }
}
