package com.repairmate.repairmate_backend.service;

import com.repairmate.repairmate_backend.InvalidCredentialsException;
import com.repairmate.repairmate_backend.dto.LoginRequest;
import com.repairmate.repairmate_backend.dto.RegisterRequest;
import com.repairmate.repairmate_backend.dto.LoginResponse;
import com.repairmate.repairmate_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.repairmate.repairmate_backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole() != null ? request.getRole() : "USER");
        userRepository.save(user);
        return "User registered successfully!";
    }

    public LoginResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            return null;
        }
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        User existingUser = userRepository.findByEmail(loginRequest.getEmail());
        if(existingUser == null || !existingUser.getPassword().equals(loginRequest.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        LoginResponse loginResponse =  new LoginResponse(existingUser.getId(), existingUser.getEmail(), existingUser.getRole());
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
