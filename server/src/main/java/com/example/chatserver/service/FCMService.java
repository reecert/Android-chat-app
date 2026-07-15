package com.example.chatserver.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    private static final Logger log = LoggerFactory.getLogger(FCMService.class);

    public void sendNotification(String targetToken, String title, String body, String chatId) {
        if (targetToken == null || targetToken.isEmpty()) {
            log.debug("Skipping notification — target token is null or empty");
            return;
        }

        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putData("chatId", chatId != null ? chatId : "")
                .build();

        try {
            String messageId = FirebaseMessaging.getInstance().send(message);
            log.info("FCM notification sent successfully: {}", messageId);
        } catch (Exception e) {
            log.error("Failed to send FCM notification to token: {}", targetToken, e);
        }
    }
}
