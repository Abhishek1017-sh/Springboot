package com.vms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        // Simple dummy login for connection testing
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Logged in successfully");
        response.put("token", "dummy-jwt-token");
        response.put("user", Map.of("name", "Test User", "email", credentials.get("email")));
        
        return ResponseEntity.ok(response);
    }
}
