package com.example.chatserver.repository;

import com.example.chatserver.model.MessageIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageIndexRepository extends JpaRepository<MessageIndex, String> {
    List<MessageIndex> findByChatId(String chatId);

    List<MessageIndex> findByTextPreviewContainingIgnoreCase(String query);
}
