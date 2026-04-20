package com.project.ecommerce.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ecommerce.dto.LoginRequestDto;
import com.project.ecommerce.dto.RegisterRequestDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void registerUser_shouldReturn201() throws Exception {

        RegisterRequestDto request = new RegisterRequestDto("test", "test123", "test@mail.com");

        mockMvc.perform(post( "/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwtToken").exists())
                .andExpect(jsonPath("$.message").value("User created"));

    }

    @Test
    void registerAdmin_shouldReturn201() throws Exception{

        RegisterRequestDto request = new RegisterRequestDto("admin", "admin789", "admin@mail.com");

        mockMvc.perform(post( "/auth/register-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwtToken").exists())
                .andExpect(jsonPath("$.message").value("Admin created"));

    }

    @Test
    void login_shouldReturn200() throws Exception{

        RegisterRequestDto registerRequest = new RegisterRequestDto("test", "test123", "test@mail.com");
        mockMvc.perform(post( "/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)));


        LoginRequestDto loginRequest = new LoginRequestDto("test@mail.com", "test123");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").exists())
                .andExpect(jsonPath("$.message").value("User logged in"));
    }

    @Test
    void login_shouldReturn403_whenLoginInfoWrong() throws Exception{
        LoginRequestDto loginRequest = new LoginRequestDto("test@mail.com", "test123");
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden());
    }


}
