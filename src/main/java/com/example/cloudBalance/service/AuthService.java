//package com.example.cloudBalance.service;
//
//import com.example.cloudBalance.dto.AuthRequest;
//import com.example.cloudBalance.dto.AuthResponse;
//import com.example.cloudBalance.entity.BlackListedToken;
//import com.example.cloudBalance.entity.User;
//import com.example.cloudBalance.repository.BlacklistedTokenRepository;
//import com.example.cloudBalance.repository.UserRepository;
//import com.example.cloudBalance.authComp.JwtService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.Optional;
//
//@Slf4j
//@Service
//public class AuthService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BlacklistedTokenRepository blacklistedTokenRepository;
//
//    @Autowired
//    private JwtService jwtService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public ResponseEntity<?> login(AuthRequest authRequest) {
//        //-----LOGGER------------------
//        log.info("Execution{}", getClass());
//        Optional<User> userOptional = userRepository.findByEmail(authRequest.getEmail());
//        if (userOptional.isEmpty()) {
//            return ResponseEntity.status(401).body("Invalid email or password");
//        }
//
//        User user = userOptional.get();
//        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
//            return ResponseEntity.status(401).body("Invalid email or password");
//        }
//
//        String roleName = user.getRoles().getName().name();
//
//        String token = jwtService.generateToken(user.getEmail(), String.valueOf(user.getRoles().getName()));
//        AuthResponse authResponse = new AuthResponse(token,user.getEmail(), roleName, user.getUsername());
//        return ResponseEntity.ok(authResponse);
//    }
//
//    public ResponseEntity<?> logout(HttpServletRequest request) {
//        final String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.badRequest().body("Invalid Authorization header");
//        }
//        String token = authHeader.substring(7);
//        Date expiration;
//
//        try {
//            String email = jwtService.extractEmail(token);
//            User user = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//            user.setLastLoginAt(LocalDateTime.now());
//            userRepository.save(user);
//            expiration = jwtService.extractExpiration(token);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Invalid JWT: " + e.getMessage());
//        }
//        blacklistedTokenRepository.save(new BlackListedToken(token, expiration));
//        SecurityContextHolder.clearContext();
//        return ResponseEntity.ok("User logged out successfully.");
//    }
//}

package com.example.cloudBalance.service;

import com.example.cloudBalance.authComp.JwtService;
import com.example.cloudBalance.dto.AuthRequest;
import com.example.cloudBalance.dto.AuthResponse;
import com.example.cloudBalance.entity.BlackListedToken;
import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.repository.BlacklistedTokenRepository;
import com.example.cloudBalance.repository.UserRepository;
import com.example.cloudBalance.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<ApiResponse<AuthResponse>> login(AuthRequest authRequest) {
        log.info("Executing login for email: {}", authRequest.getEmail());

        Optional<User> userOptional = userRepository.findByEmail(authRequest.getEmail());
        if (userOptional.isEmpty()) {
            ApiResponse<AuthResponse> response = new ApiResponse<>(401, "Invalid email or password", null);
            return ResponseEntity.status(401).body(response);
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            ApiResponse<AuthResponse> response = new ApiResponse<>(401, "Invalid email or password", null);
            return ResponseEntity.status(401).body(response);
        }

        String roleName = user.getRoles().getName().name();
        String token = jwtService.generateToken(user.getEmail(), roleName);

        AuthResponse authResponse = new AuthResponse(token, user.getEmail(), roleName, user.getUsername());

        ApiResponse<AuthResponse> response = new ApiResponse<>(200, "Login successful", authResponse);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ApiResponse<String> response = new ApiResponse<>(400, "Invalid Authorization header", null);
            return ResponseEntity.badRequest().body(response);
        }

        String token = authHeader.substring(7);
        Date expiration;

        try {
            String email = jwtService.extractEmail(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);

            expiration = jwtService.extractExpiration(token);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(400, "Invalid JWT: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }

        blacklistedTokenRepository.save(new BlackListedToken(token, expiration));
        SecurityContextHolder.clearContext();

        ApiResponse<String> response = new ApiResponse<>(200, "User logged out successfully.", null);
        return ResponseEntity.ok(response);
    }
}

