package com.easyinvest.controllers;

import com.easyinvest.controllers.UserController;
import com.easyinvest.dtos.UserCreateDTO;
import com.easyinvest.dtos.UserResponseDTO;
import com.easyinvest.dtos.UserUpdateDTO;
import com.easyinvest.entities.User;
import com.easyinvest.enums.Sex;
import com.easyinvest.exceptions.InsufficientBalanceException;
import com.easyinvest.exceptions.UserNotFoundException;
import com.easyinvest.repositories.*;
import com.easyinvest.security.AuthenticatedUserService;
import com.easyinvest.services.JwtService;
import com.easyinvest.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.postgresql.hostchooser.HostRequirement.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(ObjectMapper.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private AuthenticatedUserService authenticatedUserService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldCreateUserSucessfully() throws Exception {

        UserResponseDTO response = new UserResponseDTO(
                UUID.randomUUID().toString(),
                "Rick",
                "rickteste@gmail.com"
        );

        UserCreateDTO request = new UserCreateDTO(
                "Rick",
                "rickteste@gmail.com",
                "1234556",
                "11122233320",
                "Rua A",
                "38999552030",
                Sex.MASCULINO
        );

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldDoentsNotCreateUser() throws Exception {

        UserResponseDTO response = new UserResponseDTO(
                UUID.randomUUID().toString(),
                "Rick",
                "rickteste@gmail.com"
        );

        UserCreateDTO request = new UserCreateDTO(
                "Rick",
                "rickteste@gmail.com",
                "123",
                "11122233320",
                "Rua: A",
                "38999552020",
                Sex.MASCULINO
        );

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFoundUserById() throws Exception {

        UUID uuid = UUID.randomUUID();

        UserResponseDTO response = new UserResponseDTO(
                uuid.toString(),
                "Rick",
                "rickteste@gmail.com"
        );

        when(userService.findUserAuthenticatedById(any())).thenReturn(response);

        mockMvc.perform(get("/users/me"))
                        .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rick"))
                .andExpect(jsonPath("$.email").value("rickteste@gmail.com"));
    }

    @Test
    void shouldDoesntFoundUserById() throws Exception {

        UUID uuid = UUID.randomUUID();

        UserResponseDTO response = new UserResponseDTO(
                uuid.toString(),
                "Rick",
                "rickteste@gmail.com"
        );

        when(userService.findUserAuthenticatedById(any())).thenThrow(new UserNotFoundException("Usuário não encontrado"));

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateUser() throws Exception {

        UUID uuid = UUID.randomUUID();

        UserResponseDTO response = new UserResponseDTO(
                uuid.toString(),
                "Rick Alves",
                "rickteste123@gmail.com"
        );

        UserUpdateDTO request = new UserUpdateDTO(
                "Rick Alves",
                "rickteste123@gmail.com",
                "38999553520",
                "Rua: C",
                Sex.MASCULINO
        );

        when(userService.updateAuthenticatedUser(any(), any(UserUpdateDTO.class))).thenReturn(response);

        mockMvc.perform(put("/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rick Alves"))
                .andExpect(jsonPath("$.email").value("rickteste123@gmail.com"));
    }

    @Test
    void shouldDoesntUpdateUser() throws Exception {

        UUID uuid = UUID.randomUUID();

        UserResponseDTO response = new UserResponseDTO(
                uuid.toString(),
                "Rick Alves",
                "rickteste123@gmail.com"
        );

        UserUpdateDTO request = new UserUpdateDTO(
                "Rick Alves",
                "rickteste123@gmail.com",
                "38999553520",
                "Rua: C",
                Sex.MASCULINO
        );

        when(userService.updateAuthenticatedUser(any(), any(UserUpdateDTO.class)))
                .thenThrow(new UserNotFoundException("Usuário não encontrado"));

        mockMvc.perform(put("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteUser() throws Exception {

        doNothing().when(userService).deleteAuthenticatedUser(any());

        mockMvc.perform(delete("/users/me"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDoesNotDeleteUser() throws Exception {

        doThrow(new UserNotFoundException("Usuário não encontrado"))
                .when(userService).deleteAuthenticatedUser(any());
        mockMvc.perform(delete("/users/me"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetUserBalance() throws Exception {

        when(userService.getAuthenticatedUserBalance(any()))
                .thenReturn(BigDecimal.valueOf(5000.00));
        mockMvc.perform(get("/users/me/wallet"))
                .andExpect(status().isOk())
                .andExpect(content().string("5000.0"));
    }

    @Test
    void shouldDoesntGetUserBalance() throws Exception {

            when(userService.getAuthenticatedUserBalance(any()))
                    .thenThrow(new InsufficientBalanceException("Saldo Indisponível"));
            mockMvc.perform(get("/users/me/wallet"))
                    .andExpect(status().isBadRequest());
    }
}
