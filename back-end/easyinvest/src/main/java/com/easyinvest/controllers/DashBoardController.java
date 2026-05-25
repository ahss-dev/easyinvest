package com.easyinvest.controllers;

import com.easyinvest.dtos.DashBoardResponseDTO;
import com.easyinvest.entities.User;
import com.easyinvest.security.AuthenticatedUserService;
import com.easyinvest.services.DashBoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {

    private final DashBoardService dashBoardService;
    private final AuthenticatedUserService authenticatedUserService;

    public DashBoardController(DashBoardService dashBoardService,  AuthenticatedUserService authenticatedUserService) {
        this.dashBoardService = dashBoardService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping("/summary/me")
    public ResponseEntity<DashBoardResponseDTO>
    getDashBoard() {

        User user = (User) authenticatedUserService.getAuthenticatedUser();

        return ResponseEntity.ok(dashBoardService.getDashBoardSummary(user));
    }
}