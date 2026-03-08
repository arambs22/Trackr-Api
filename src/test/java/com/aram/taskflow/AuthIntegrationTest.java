package com.aram.taskflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void register_returnsToken() throws Exception {
        String email = "testuser_" + System.currentTimeMillis() + "@test.com";
        String body = """
                {
                "name": "Test User",
                "email": "%s",
                "password": "Password123!"
                }
                """.formatted(email);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
        }
        
        @Test
        void login_returnsToken() throws Exception {
                String email = "testuser_" + System.currentTimeMillis() + "@test.com";

                String register = """
                        {
                        "name": "Test User 2",
                        "email": "%s",
                        "password": "Password123!"
                        }
                        """.formatted(email);

                mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(register))
                        .andExpect(status().isOk());

                String login = """
                        {
                        "email": "%s",
                        "password": "Password123!"
                        }
                        """.formatted(email);

                mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.token").isNotEmpty());
        }
}