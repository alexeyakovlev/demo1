package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.TokenPair;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth controller", description = "APIs for registering new users, logging in, and updating the token")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "User created successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid user input")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest)
            throws UserAlreadyExistsException {
        authService.registerUser(registerRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @Operation(summary = "User login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "User has successfully logged in"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid user input")
    })
    @PostMapping("/login")
    public  ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        TokenPair  tokenPair = authService.login(loginRequest);
        return ResponseEntity.ok(tokenPair);
    }

    @Operation(summary = "Token update")
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        TokenPair tokenPair = authService.refreshToken(request);
        return ResponseEntity.ok(tokenPair);
    }
}
