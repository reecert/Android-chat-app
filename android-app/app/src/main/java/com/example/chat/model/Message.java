package com.example.chat.model;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.Map;

@IgnoreExtraProperties
public class Message {
    public String messageId;
    public String senderId;
    public String text;
    public long createdAt;
    public Map<String, Object> status; // SENT, DELIVERED, READ

    public Message() {
    }

    public Message(String messageId, String senderId, String text, long createdAt) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.text = text;
        this.createdAt = createdAt;
    }
}
