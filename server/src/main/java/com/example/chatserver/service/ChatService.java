package com.example.chatserver.service;

import com.example.chatserver.model.ChatMetadata;
import com.example.chatserver.model.MessageIndex;
import com.example.chatserver.repository.ChatMetadataRepository;
import com.example.chatserver.repository.MessageIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final ChatMetadataRepository chatRepository;
    private final MessageIndexRepository messageRepository;

    public ChatService(ChatMetadataRepository chatRepository, MessageIndexRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public void upsertChatMetadata(String chatId, List<String> participantUids, String lastMessage) {
        ChatMetadata metadata = chatRepository.findById(chatId).orElse(new ChatMetadata());
        metadata.setChatId(chatId);
        metadata.setParticipantUids(participantUids);
        metadata.setLastMessage(lastMessage);
        metadata.setUpdatedAt(Instant.now());
        chatRepository.save(metadata);
        log.debug("Upserted chat metadata for chatId={}", chatId);
    }

    @Transactional
    public void indexMessage(String messageId, String chatId, String senderId, String text) {
        MessageIndex index = new MessageIndex();
        index.setMessageId(messageId);
        index.setChatId(chatId);
        index.setSenderId(senderId);

        // Null-safe text truncation
        if (text != null) {
            index.setTextPreview(text.length() > 500 ? text.substring(0, 500) : text);
        } else {
            index.setTextPreview("");
        }

        index.setCreatedAt(Instant.now());
        messageRepository.save(index);
        log.debug("Indexed message {} in chat {}", messageId, chatId);
    }

    public List<ChatMetadata> searchChats(String query) {
        return chatRepository.findByLastMessageContainingIgnoreCase(query);
    }
}
