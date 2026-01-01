package com.example.chatserver;

import com.example.chatserver.model.ChatMetadata;
import com.example.chatserver.repository.ChatMetadataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RepositoryTest {

    @Autowired
    private ChatMetadataRepository chatRepository;

    @Test
    public void testSaveAndFind() {
        ChatMetadata chat = new ChatMetadata("chat1", List.of("u1", "u2"), "hello", Instant.now());
        chatRepository.save(chat);

        List<ChatMetadata> found = chatRepository.findByLastMessageContainingIgnoreCase("HELLO");
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getChatId()).isEqualTo("chat1");
    }
}
