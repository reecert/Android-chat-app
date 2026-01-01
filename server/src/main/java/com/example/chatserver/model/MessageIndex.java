package com.example.chatserver.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "message_index")
public class MessageIndex {
    @Id
    private String messageId;

    private String chatId;
    private String senderId;

    @Column(length = 500)
    private String textPreview;

    private Instant createdAt;

    public MessageIndex() {
    }

    public MessageIndex(String messageId, String chatId, String senderId, String textPreview, Instant createdAt) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.senderId = senderId;
        this.textPreview = textPreview;
        this.createdAt = createdAt;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTextPreview() {
        return textPreview;
    }

    public void setTextPreview(String textPreview) {
        this.textPreview = textPreview;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
