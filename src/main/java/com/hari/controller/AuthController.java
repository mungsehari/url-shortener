package com.hari.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hari.dtos.LoginRequest;
import com.hari.dtos.RegisterRequest;
import com.hari.model.User;
import com.hari.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @CrossOrigin("http://localhost:5173")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.authenticateUser(request));
    }

    @PostMapping("/register")
    @CrossOrigin("http://localhost:5173")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole("ROLE_USER");
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully!");

    }

}
