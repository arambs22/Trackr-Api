package com.aram.taskflow;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ListsIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    private String registerAndLogin(String email) throws Exception {

        String register = """
                {
                  "name": "Test",
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

        var res = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(login))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(res).get("token").asText();
    }

    @Test
    void createList_insideBoard() throws Exception {

        String email = "listtest_" + System.currentTimeMillis() + "@test.com";
        String token = registerAndLogin(email);

        String createBoard = """
                { "name": "Board Test" }
                """;

        var boardRes = mockMvc.perform(post("/boards")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createBoard))
                .andReturn().getResponse().getContentAsString();

        Long boardId = objectMapper.readTree(boardRes).get("id").asLong();

        String createList = """
                {
                  "name": "Todo",
                  "position": 0
                }
                """;

        mockMvc.perform(post("/boards/" + boardId + "/lists")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createList))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Todo"));
    }
}