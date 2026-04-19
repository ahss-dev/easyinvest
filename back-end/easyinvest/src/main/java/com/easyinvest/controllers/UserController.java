package com.easyinvest.controllers;

import com.easyinvest.dto.UserCreateDTO;
import com.easyinvest.dto.UserResponseDTO;
import com.easyinvest.dto.UserUpdateDTO;
import com.easyinvest.entities.User;
import com.easyinvest.services.UserService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    //post não pode retornar nada, mudar depois, apenas criar.
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserCreateDTO dto) {
        UserResponseDTO user = service.createUser(dto);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/id")
    public ResponseEntity<UserResponseDTO> findById(@RequestParam String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable String id,
            @RequestBody UserUpdateDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/wallet")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String id) {
        BigDecimal balance = service.getUserBalance(id);
        return ResponseEntity.ok(balance);
    }
}