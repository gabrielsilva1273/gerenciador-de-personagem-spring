package com.gerenciadordepersonagem.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerenciadordepersonagem.security.model.RegistrationRequest;
import com.gerenciadordepersonagem.security.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegistrationService service;

    @Test
    void nullUserNameShouldReturnBadRequest () throws Exception {
        RegistrationRequest request =
                new RegistrationRequest(null, "emilio@gmail.com",
                        "l#dvaZ$1qV8GIw","l#dvaZ$1qV8GIw");
        mockMvc.perform(post("/auth/registrar").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))).
                andExpect(status().isBadRequest());
    }
    @Test
    void nullEmailShouldReturnBadRequest () throws Exception {
        RegistrationRequest request =
                new RegistrationRequest("emilio", null,
                        "l#dvaZ$1qV8GIw","l#dvaZ$1qV8GIw");
        mockMvc.perform(post("/auth/registrar").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))).
                andExpect(status().isBadRequest());
    }
    @Test
    void nullPasswordShouldReturnBadRequest () throws Exception {
        RegistrationRequest request =
                new RegistrationRequest("emilio", "emilio@gmail.com", null,null);
        mockMvc.perform(post("/auth/registrar").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))).
                andExpect(status().isBadRequest());
    }
    @Test
    void blankUserNameShouldReturnBadRequest () throws Exception {
        RegistrationRequest request =
                new RegistrationRequest("", "emilio@gmail.com",
                        "l#dvaZ$1qV8GIw","l#dvaZ$1qV8GIw");
        mockMvc.perform(post("/auth/registrar").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))).
                andExpect(status().isBadRequest());
    }
    @Test
    void blankEmailShouldReturnBadRequest () throws Exception {
        RegistrationRequest request =
                new RegistrationRequest("emilio", "",
                        "l#dvaZ$1qV8GIw","l#dvaZ$1qV8GIw");
        mockMvc.perform(post("/auth/registrar").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))).
                andExpect(status().isBadRequest());
    }
    @Test
    void blankPasswordShouldReturnBadRequest () throws Exception {
        RegistrationRequest request =
                new RegistrationRequest("emilio", "emilio@gmail.com", ""," ");
        mockMvc.perform(post("/auth/registrar").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))).
                andExpect(status().isBadRequest());
    }
    @Test
    void thirteenCharactersPasswordShouldReturnBadRequest() throws Exception{
        RegistrationRequest request =
                new RegistrationRequest("emilio", "emilio@gmail.com",
                        "%7o31@tNJzK+s","%7o31@tNJzK+s");
        mockMvc.perform(post("/auth/registrar").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))).
                andExpect(status().isBadRequest());
    }
    @Test
    void fourteenCharactersPasswordShouldOk() throws Exception{
        RegistrationRequest request =
                new RegistrationRequest("emilio", "emilio@gmail.com",
                        "K0Fy@-KLvzuth1","K0Fy@-KLvzuth1");
        mockMvc.perform(post("/auth/registrar").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))).
                andExpect(status().isOk());
    }
    @Test
    void oneThousandAndOneCharactersPasswordShouldReturnBadRequest() throws Exception{
        StringBuilder password = new StringBuilder();
        password.append("a".repeat(1001));

        RegistrationRequest request =
                new RegistrationRequest("emilio",
                        "emilio@gmail.com", password.toString(), password.toString());
        mockMvc.perform(post("/auth/registrar").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))).
                andExpect(status().isBadRequest());
    }
    @Test
    void oneThousandCharactersPasswordShouldOk() throws Exception{
        StringBuilder password = new StringBuilder();
        password.append("a".repeat(1000));

        RegistrationRequest request =
                new RegistrationRequest("emilio", "emilio@gmail.com",
                        password.toString(),password.toString());
        mockMvc.perform(post("/auth/registrar").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))).
                andExpect(status().isOk());
    }
    @Test
    void validRequestShouldReturnOk() throws Exception{
        RegistrationRequest request =
                new RegistrationRequest("emilio", "emilio@gmail.com",
                        "-8e3q@xeuwFOVuHfc4o2","-8e3q@xeuwFOVuHfc4o2");
        mockMvc.perform(post("/auth/registrar").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))).
                andExpect(status().isOk());
    }


}