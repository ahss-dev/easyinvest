package com.easyinvest.controllers;

import com.easyinvest.dtos.OrderRequestDTO;
import com.easyinvest.entities.User;
import com.easyinvest.enums.Sex;
import com.easyinvest.enums.TransactionType;
import com.easyinvest.security.AuthenticatedUserService;
import com.easyinvest.security.JwtAuthenticationFilter;
import com.easyinvest.services.JwtService;
import com.easyinvest.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private AuthenticatedUserService authenticatedUserService;

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {

        User user = new User(
                "Rick",
                "rick@gmail.com",
                "123456",
                "11122233320"
        );

        user.updateContactInfo(
                "38999552030",
                "Rua A",
                Sex.MASCULINO
        );

        OrderRequestDTO request = new OrderRequestDTO();

        request.setAssetId(UUID.randomUUID());
        request.setQuantity(10);
        request.setTransactionType(TransactionType.COMPRA);

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        doNothing().when(orderService)
                .createOrder(any(OrderRequestDTO.class), any(User.class));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}