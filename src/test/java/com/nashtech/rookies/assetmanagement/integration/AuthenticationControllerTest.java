package com.nashtech.rookies.assetmanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.rookies.assetmanagement.dto.request.AuthenticationRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private AuthenticationRequest validRequest;

    private AuthenticationRequest invalidRequest;

    @BeforeEach
    void setUp() {
        validRequest = AuthenticationRequest.builder()
                .username("nhatt")
                .password("Nhat@123")
                .build();

        invalidRequest = AuthenticationRequest.builder()
                .username("nguyen")
                .password("Nguyen@123")
                .build();
    }

    @Test
    public void testSignIn_whenValidRequest_thenSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testSignIn_whenInvalidRequest_thenThrow() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/auth/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", Matchers.is("Username or password is incorrect. Please try again.")));
    }

}
