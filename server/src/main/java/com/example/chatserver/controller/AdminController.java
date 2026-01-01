package com.example.chatserver.controller;

import com.example.chatserver.model.ChatMetadata;
import com.example.chatserver.model.ModerationReport;
import com.example.chatserver.service.ChatService;
import com.example.chatserver.service.ModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ModerationService moderationService;

    @GetMapping("/chats")
    public List<ChatMetadata> searchChats(@RequestParam("query") String query) {
        return chatService.searchChats(query);
    }

    @PostMapping("/reports")
    public ModerationReport createReport(@RequestBody ReportRequest request) {
        return moderationService.createReport(request.getChatId(), request.getMessageId(), request.getReporterUid(),
                request.getReason());
    }

    @GetMapping("/reports")
    public List<ModerationReport> getReports(@RequestParam("status") String status) {
        return moderationService.getReports(ModerationReport.ReportStatus.valueOf(status));
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
