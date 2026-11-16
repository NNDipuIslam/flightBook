package com.classz.flightbook.auth.controller;

import com.classz.flightbook.auth.dto.AuthResponse;
import com.classz.flightbook.auth.dto.LoginRequest;
import com.classz.flightbook.auth.dto.LogoutRequest;
import com.classz.flightbook.auth.dto.RegisterRequest;
import com.classz.flightbook.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse authResponse = authService.register(registerRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody(required = false) LogoutRequest logoutRequest) {

        String token = null;
        if (logoutRequest != null && logoutRequest.getToken() != null && !logoutRequest.getToken().isBlank()) {
            token = logoutRequest.getToken();
        } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token != null) {
            authService.logout(token);
        }

        return ResponseEntity.noContent().build();
    }
}
