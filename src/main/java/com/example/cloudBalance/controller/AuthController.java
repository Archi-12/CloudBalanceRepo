//package com.example.cloudBalance.controller;
//
//import com.example.cloudBalance.dto.AuthRequest;
//import com.example.cloudBalance.dto.UserResponse;
//import com.example.cloudBalance.entity.User;
//import com.example.cloudBalance.repository.UserRepository;
//import com.example.cloudBalance.service.AuthService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
//        return authService.login(authRequest);
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request) {
//        return authService.logout(request);
//    }
//
//    @GetMapping("/me")
//        public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
//        String email = authentication.getName(); // JWT gives this via SecurityContext
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        UserResponse response = new UserResponse(
//                user.getUsername(),
//                user.getEmail(),
//                user.getRoles().toString(),
//                user.getLastLoginAt(),
//                user.getAccount()
//        );
//
//        return ResponseEntity.ok(response);
//    }
//}

package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.AuthRequest;
import com.example.cloudBalance.dto.AuthResponse;
import com.example.cloudBalance.dto.UserResponse;
import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.repository.UserRepository;
import com.example.cloudBalance.dto.ApiResponse;
import com.example.cloudBalance.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        return authService.logout(request);
        // Same, service should return ApiResponse
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = new UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRoles().toString(),
                user.getLastLoginAt(),
                user.getAccount()
        );

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(
                200,
                "Current user fetched successfully",
                response
        );

        return ResponseEntity.ok(apiResponse);
    }
}

