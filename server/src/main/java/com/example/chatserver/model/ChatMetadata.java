package com.example.chatserver.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "chat_metadata")
public class ChatMetadata {
    @Id
    private String chatId;

    @ElementCollection
    private List<String> participantUids;

    private String lastMessage;

    private Instant updatedAt;

    public ChatMetadata() {
    }

    public ChatMetadata(String chatId, List<String> participantUids, String lastMessage, Instant updatedAt) {
        this.chatId = chatId;
        this.participantUids = participantUids;
        this.lastMessage = lastMessage;
        this.updatedAt = updatedAt;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<String> getParticipantUids() {
        return participantUids;
    }

    public void setParticipantUids(List<String> participantUids) {
        this.participantUids = participantUids;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
