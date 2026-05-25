package com.easyinvest.controllers;

import com.easyinvest.dtos.PositionResponseDTO;
import com.easyinvest.entities.User;
import com.easyinvest.security.AuthenticatedUserService;
import com.easyinvest.services.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionService positionService;
    private final AuthenticatedUserService authenticatedUserService;

    @Autowired
    public PositionController(PositionService positionService, AuthenticatedUserService authenticatedUserService) {
        this.positionService = positionService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<PositionResponseDTO>> getPositions() {

        User user = authenticatedUserService.getAuthenticatedUser();

        List<PositionResponseDTO> positions = positionService.getUserPositions(user);

        return ResponseEntity.ok(positions);
    }
}