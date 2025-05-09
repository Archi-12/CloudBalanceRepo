package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.RegisterRequest;
import com.example.cloudBalance.dto.UserResponse;
import com.example.cloudBalance.entity.Accounts;
import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.repository.UserRepository;
import com.example.cloudBalance.dto.ApiResponse;
import com.example.cloudBalance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN') or hasRole('READ_ONLY')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RegisterRequest>>> getAllUsers(Authentication authentication) {
        List<RegisterRequest> users = userService.getAllUsers(authentication);
        ApiResponse<List<RegisterRequest>> response = new ApiResponse<>(200, "Users fetched successfully", users);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest request) {
        UserResponse registeredUser = userService.saveUser(request);
        ApiResponse<UserResponse> response = new ApiResponse<>(201, "User registered successfully", registeredUser);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse responseData = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRoles().getName().name(),
                user.getLastLoginAt(),
                user.getAccount()
        );

        ApiResponse<UserResponse> response = new ApiResponse<>(200, "Current user fetched successfully", responseData);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id, @RequestBody RegisterRequest updatedRequest) {
        UserResponse updatedUser = userService.updateUser(id, updatedRequest);
        ApiResponse<UserResponse> response = new ApiResponse<>(200, "User updated successfully", updatedUser);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Set<Accounts>>> getUserAccounts(@PathVariable Long id) {
        Set<Accounts> accounts = userService.getUserAccounts(id);
        ApiResponse<Set<Accounts>> response = new ApiResponse<>(200, "User accounts fetched successfully", accounts);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("customers")
    public ResponseEntity<ApiResponse<List<RegisterRequest>>> getAllCustomers() {
        List<RegisterRequest> customers = userService.getAllCustomers();
        ApiResponse<List<RegisterRequest>> response = new ApiResponse<>(200, "Customers fetched successfully", customers);
        return ResponseEntity.ok(response);
    }

}
