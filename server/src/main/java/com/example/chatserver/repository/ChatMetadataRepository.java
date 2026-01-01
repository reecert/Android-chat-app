package com.example.chatserver.repository;

import com.example.chatserver.model.ChatMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMetadataRepository extends JpaRepository<ChatMetadata, String> {
    List<ChatMetadata> findByParticipantUidsContaining(String uid);

    List<ChatMetadata> findByLastMessageContainingIgnoreCase(String query);
}
