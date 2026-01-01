package com.example.chatserver.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actorUid;
    private String action;
    private String targetId;

    private Instant createdAt;

    public AuditLog() {
    }

    public AuditLog(Long id, String actorUid, String action, String targetId, Instant createdAt) {
        this.id = id;
        this.actorUid = actorUid;
        this.action = action;
        this.targetId = targetId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActorUid() {
        return actorUid;
    }

    public void setActorUid(String actorUid) {
        this.actorUid = actorUid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
