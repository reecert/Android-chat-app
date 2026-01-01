package com.example.chatserver.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "moderation_reports")
public class ModerationReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private String chatId;
    private String messageId;
    private String reporterUid;
    private String reason;

    @Enumerated(EnumType.STRING)
    private ReportStatus status; // OPEN, RESOLVED

    private Instant createdAt;
    private Instant resolvedAt;

    public enum ReportStatus {
        OPEN, RESOLVED
    }

    public ModerationReport() {
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getReporterUid() {
        return reporterUid;
    }

    public void setReporterUid(String reporterUid) {
        this.reporterUid = reporterUid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}
