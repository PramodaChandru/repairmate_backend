package com.repairmate.repairmate_backend.controller;

import com.repairmate.repairmate_backend.InvalidCredentialsException;
import com.repairmate.repairmate_backend.dto.LoginRequest;
import com.repairmate.repairmate_backend.dto.RegisterRequest;
import com.repairmate.repairmate_backend.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.repairmate.repairmate_backend.repository.UserRepository;
import com.repairmate.repairmate_backend.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @GetMapping("/greet")
    public String greet() {
        return "Hello, welcome to RepairMate application";
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String message = authService.register(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return authService.login(loginRequest);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        LoginResponse response = authService.getUserByEmail(email);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email : " + email);
        }

        return ResponseEntity.ok(response);
    }
}