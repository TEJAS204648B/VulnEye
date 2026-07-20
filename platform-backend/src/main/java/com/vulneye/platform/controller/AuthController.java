package com.vulneye.platform.controller;

import com.vulneye.platform.dto.ApiResponse;
import com.vulneye.platform.dto.auth.LoginRequest;
import com.vulneye.platform.service.interfaces.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        authenticationService.authenticate(loginRequest);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Login successful",
                        "Authentication successful"
                )
        );
    }
}