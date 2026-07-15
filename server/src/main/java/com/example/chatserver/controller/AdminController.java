package com.example.chatserver.controller;

import com.example.chatserver.model.ChatMetadata;
import com.example.chatserver.model.ModerationReport;
import com.example.chatserver.service.ChatService;
import com.example.chatserver.service.ModerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ChatService chatService;
    private final ModerationService moderationService;

    public AdminController(ChatService chatService, ModerationService moderationService) {
        this.chatService = chatService;
        this.moderationService = moderationService;
    }

    @GetMapping("/chats")
    public List<ChatMetadata> searchChats(@RequestParam("query") String query) {
        return chatService.searchChats(query);
    }

    @PostMapping("/reports")
    public ResponseEntity<?> createReport(@RequestBody ReportRequest request) {
        if (request.getChatId() == null || request.getMessageId() == null || request.getReporterUid() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "chatId, messageId, and reporterUid are required"));
        }
        ModerationReport report = moderationService.createReport(
                request.getChatId(), request.getMessageId(), request.getReporterUid(), request.getReason());
        return ResponseEntity.ok(report);
    }

    @GetMapping("/reports")
    public ResponseEntity<?> getReports(@RequestParam("status") String status) {
        ModerationReport.ReportStatus reportStatus;
        try {
            reportStatus = ModerationReport.ReportStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Invalid status. Must be one of: OPEN, RESOLVED"));
        }
        return ResponseEntity.ok(moderationService.getReports(reportStatus));
    }

    @PostMapping("/reports/{reportId}/resolve")
    public ResponseEntity<?> resolveReport(@PathVariable Long reportId) {
        moderationService.resolveReport(reportId);
        return ResponseEntity.ok(Map.of("status", "resolved"));
    }

    public static class ReportRequest {
        private String chatId;
        private String messageId;
        private String reporterUid;
        private String reason;

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
    }
}
