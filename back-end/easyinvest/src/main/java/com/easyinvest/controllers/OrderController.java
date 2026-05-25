package com.easyinvest.controllers;

import com.easyinvest.dtos.OrderRequestDTO;
import com.easyinvest.dtos.OrderResponseDTO;
import com.easyinvest.entities.User;
import com.easyinvest.enums.TradeType;
import com.easyinvest.enums.TransactionType;
import com.easyinvest.exceptions.InsufficientBalanceException;
import com.easyinvest.security.AuthenticatedUserService;
import com.easyinvest.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final AuthenticatedUserService authenticatedUserService;

    public OrderController(OrderService orderService, AuthenticatedUserService authenticatedUserService) {
        this.orderService = orderService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @PostMapping
    public ResponseEntity<Void> CreateOrder(@RequestBody @Valid OrderRequestDTO dto) throws InsufficientBalanceException {
        User user = authenticatedUserService.getAuthenticatedUser();
        orderService.createOrder(dto, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(@RequestParam(required = false)
                                                               TransactionType transactionType,
                                                               @RequestParam(required = false)
                                                               TradeType tradeType) {

        User user = authenticatedUserService.getAuthenticatedUser();

        List<OrderResponseDTO> orders = orderService.getUserOrdersByUser(user, transactionType, tradeType);
        return ResponseEntity.ok(orders);
    }
}