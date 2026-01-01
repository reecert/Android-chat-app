package com.example.chatserver.controller;

import com.example.chatserver.service.ChatService;
import com.example.chatserver.service.FCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    @Autowired
    private FCMService fcmService;

    @Autowired
    private ChatService chatService;

    @PostMapping("/message")
    public ResponseEntity<?> notifyMessage(@RequestBody MessageSyncRequest request,
            @AuthenticationPrincipal Object principal) {
        chatService.upsertChatMetadata(request.getChatId(), request.getParticipantUids(), request.getText());
        chatService.indexMessage(request.getMessageId(), request.getChatId(), request.getSenderId(), request.getText());

        if (request.getTargetTokens() != null) {
            for (String token : request.getTargetTokens()) {
                fcmService.sendNotification(token, "New Message", request.getSenderName() + ": " + request.getText(),
                        request.getChatId());
            }
        }
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
