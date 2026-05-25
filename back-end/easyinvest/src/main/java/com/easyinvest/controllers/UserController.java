package com.easyinvest.controllers;

import com.easyinvest.dtos.UserCreateDTO;
import com.easyinvest.dtos.UserResponseDTO;
import com.easyinvest.dtos.UserUpdateDTO;
import com.easyinvest.entities.User;
import com.easyinvest.security.AuthenticatedUserService;
import com.easyinvest.services.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final AuthenticatedUserService authenticatedUserService;

    public UserController(UserService service,  AuthenticatedUserService authenticatedUserService) {
        this.service = service;
        this.authenticatedUserService = authenticatedUserService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid UserCreateDTO dto) {
        service.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> findUserAuthenticatedById() {
        UserResponseDTO response = service.findUserAuthenticatedById(authenticatedUserService.getAuthenticatedUser());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateAuthenticatedUser(
            @RequestBody UserUpdateDTO dto
    ) {
        UserResponseDTO response = service.updateAuthenticatedUser(authenticatedUserService.getAuthenticatedUser(), dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAuthenticatedUser() {
        service.deleteAuthenticatedUser(authenticatedUserService.getAuthenticatedUser());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/wallet")
    public ResponseEntity<BigDecimal> getBalance() {
        BigDecimal balance = service.getAuthenticatedUserBalance(authenticatedUserService.getAuthenticatedUser());
        return ResponseEntity.ok(balance);
    }
}