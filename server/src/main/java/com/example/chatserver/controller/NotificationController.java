package com.example.chatserver.controller;

import com.example.chatserver.service.ChatService;
import com.example.chatserver.service.FCMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final FCMService fcmService;
    private final ChatService chatService;

    public NotificationController(FCMService fcmService, ChatService chatService) {
        this.fcmService = fcmService;
        this.chatService = chatService;
    }

    @PostMapping("/message")
    public ResponseEntity<?> notifyMessage(@RequestBody MessageSyncRequest request) {
        // Validate required fields
        if (request.getChatId() == null || request.getMessageId() == null || request.getSenderId() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "chatId, messageId, and senderId are required"));
        }

        chatService.upsertChatMetadata(request.getChatId(), request.getParticipantUids(), request.getText());
        chatService.indexMessage(request.getMessageId(), request.getChatId(), request.getSenderId(), request.getText());

        if (request.getTargetTokens() != null && !request.getTargetTokens().isEmpty()) {
            String notificationBody = (request.getSenderName() != null ? request.getSenderName() : "Someone")
                    + ": " + (request.getText() != null ? request.getText() : "");
            for (String token : request.getTargetTokens()) {
                fcmService.sendNotification(token, "New Message", notificationBody, request.getChatId());
            }
        }

        log.info("Synced message {} in chat {}", request.getMessageId(), request.getChatId());
        return ResponseEntity.ok(Map.of("status", "synced_and_notified"));
    }

    public static class MessageSyncRequest {
        private String chatId;
        private String messageId;
        private String senderId;
        private String senderName;
        private String text;
        private List<String> participantUids;
        private List<String> targetTokens;

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

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<String> getParticipantUids() {
            return participantUids;
        }

        public void setParticipantUids(List<String> participantUids) {
            this.participantUids = participantUids;
        }

        public List<String> getTargetTokens() {
            return targetTokens;
        }

        public void setTargetTokens(List<String> targetTokens) {
            this.targetTokens = targetTokens;
        }
    }
}
