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
class BoardOwnershipTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    private String registerAndLogin(String name, String email) throws Exception {
        String register = """
                {
                  "name": "%s",
                  "email": "%s",
                  "password": "Password123!"
                }
                """.formatted(name, email);

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
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(res);
        return json.get("token").asText();
    }

    @Test
    void userB_cannotAccess_userA_board() throws Exception {
        long stamp = System.currentTimeMillis();
        String tokenA = registerAndLogin("UserA", "usera_" + stamp + "@test.com");
        String tokenB = registerAndLogin("UserB", "userb_" + (stamp + 1) + "@test.com");

        // A crea board
        String createBoard = """
                { "name": "A Board" }
                """;

        var createRes = mockMvc.perform(post("/boards")
                .header("Authorization", "Bearer " + tokenA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createBoard))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long boardId = objectMapper.readTree(createRes).get("id").asLong();

        // B intenta leer el board de A -> debe fallar
        // Dependiendo de tu implementación puede ser 400/404.
        mockMvc.perform(get("/boards/" + boardId)
                .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isBadRequest()); // si a ti te da 404, lo cambiamos
    }
}