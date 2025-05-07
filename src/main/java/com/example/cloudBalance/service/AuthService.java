package com.example.cloudBalance.service;

import com.example.cloudBalance.authComp.JwtService;
import com.example.cloudBalance.dto.AuthRequest;
import com.example.cloudBalance.dto.AuthResponse;
import com.example.cloudBalance.entity.BlackListedToken;
import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.exception.ForbiddenException;
import com.example.cloudBalance.exception.ResourceNotFound;
import com.example.cloudBalance.exception.UnauthorizedException;
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

        if (authRequest.getEmail() == null || authRequest.getPassword() == null) {
            throw new UnauthorizedException("Email and password must be provided");
        }
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String roleName = user.getRoles().getName().name();
        String token = jwtService.generateToken(user.getEmail(), roleName);
        if(user.getLastLoginAt() == null) {
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
        } else {
            LocalDateTime lastLoginAt = user.getLastLoginAt();
            if (lastLoginAt != null) {
                user.setLastLoginAt(LocalDateTime.now());
                userRepository.save(user);
            }
        }

        AuthResponse authResponse = new AuthResponse(token, user.getEmail());

        ApiResponse<AuthResponse> response = new ApiResponse<>(200, "Login successful", authResponse);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ForbiddenException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Date expiration;
        String email = jwtService.extractEmail(token);

        if (jwtService.isTokenValid(token,email)) {
            expiration = jwtService.extractExpiration(token);
        } else {
            throw new UnauthorizedException("Invalid or expired JWT token");
        }

        blacklistedTokenRepository.save(new BlackListedToken(token, expiration));
        SecurityContextHolder.clearContext();

        ApiResponse<String> response = new ApiResponse<>(200, "User logged out successfully.", null);
        return ResponseEntity.ok(response);
    }

}

