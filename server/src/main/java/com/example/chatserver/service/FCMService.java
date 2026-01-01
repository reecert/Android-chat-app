package com.example.chatserver.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    public void sendNotification(String targetToken, String title, String body, String chatId) {
        if (targetToken == null || targetToken.isEmpty())
            return;

        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putData("chatId", chatId)
                .putData("click_action", "FLUTTER_NOTIFICATION_CLICK") // or match Android Intent filter
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
