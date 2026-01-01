package com.example.chatserver.service;

import com.example.chatserver.model.ModerationReport;
import com.example.chatserver.repository.ModerationReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ModerationService {

    @Autowired
    private ModerationReportRepository repository;

    public ModerationReport createReport(String chatId, String messageId, String reporterUid, String reason) {
        ModerationReport report = new ModerationReport();
        report.setChatId(chatId);
        report.setMessageId(messageId);
        report.setReporterUid(reporterUid);
        report.setReason(reason);
        report.setStatus(ModerationReport.ReportStatus.OPEN);
        report.setCreatedAt(Instant.now());
        return repository.save(report);
    }

    public void resolveReport(Long reportId) {
        repository.findById(reportId).ifPresent(report -> {
            report.setStatus(ModerationReport.ReportStatus.RESOLVED);
            report.setResolvedAt(Instant.now());
            repository.save(report);
        });
    }

    public List<ModerationReport> getReports(ModerationReport.ReportStatus status) {
        return repository.findByStatus(status);
    }
}
