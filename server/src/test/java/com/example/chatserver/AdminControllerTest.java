package com.example.chatserver;

import com.example.chatserver.controller.AdminController;
import com.example.chatserver.service.ChatService;
import com.example.chatserver.service.ModerationService;
import com.example.chatserver.config.SecurityConfig;
import com.example.chatserver.security.FirebaseTokenFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @MockBean
    private ModerationService moderationService;

    @MockBean
    private FirebaseTokenFilter firebaseTokenFilter; // Mock the filter to bypass partial execution

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSearchChats_Authorized() throws Exception {
        mockMvc.perform(get("/admin/chats?query=test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testSearchChats_Forbidden() throws Exception {
        // Should be 403 because AdminController requires ADMIN role
        mockMvc.perform(get("/admin/chats?query=test"))
                .andExpect(status().isForbidden());
    }
}
