package com.example.chatserver.service;

import com.example.chatserver.model.ChatMetadata;
import com.example.chatserver.model.MessageIndex;
import com.example.chatserver.repository.ChatMetadataRepository;
import com.example.chatserver.repository.MessageIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMetadataRepository chatRepository;

    @Autowired
    private MessageIndexRepository messageRepository;

    @Transactional
    public void upsertChatMetadata(String chatId, List<String> participantUids, String lastMessage) {
        ChatMetadata metadata = chatRepository.findById(chatId).orElse(new ChatMetadata());
        metadata.setChatId(chatId);
        metadata.setParticipantUids(participantUids);
        metadata.setLastMessage(lastMessage);
        metadata.setUpdatedAt(Instant.now());
        chatRepository.save(metadata);
    }

    @Transactional
    public void indexMessage(String messageId, String chatId, String senderId, String text) {
        MessageIndex index = new MessageIndex();
        index.setMessageId(messageId);
        index.setChatId(chatId);
        index.setSenderId(senderId);
        index.setTextPreview(text.length() > 500 ? text.substring(0, 500) : text);
        index.setCreatedAt(Instant.now());
        messageRepository.save(index);
    }

    public List<ChatMetadata> searchChats(String query) {
        return chatRepository.findByLastMessageContainingIgnoreCase(query);
    }
}
