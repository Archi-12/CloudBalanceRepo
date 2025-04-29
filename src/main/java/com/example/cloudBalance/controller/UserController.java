//package com.example.cloudBalance.controller;
//
//import com.example.cloudBalance.dto.RegisterRequest;
//import com.example.cloudBalance.dto.UserResponse;
//import com.example.cloudBalance.entity.Accounts;
//import com.example.cloudBalance.entity.User;
//import com.example.cloudBalance.repository.UserRepository;
//import com.example.cloudBalance.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Set;
//
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @PreAuthorize("hasRole('ADMIN') or hasRole('READ_ONLY')")
//    @GetMapping
//    public ResponseEntity<List<RegisterRequest>> getAllUsers() {
//        List<RegisterRequest> users = userService.getAllUsers();
//        return ResponseEntity.ok(users);
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping
//    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
//        UserResponse registeredUser = userService.saveUser(request);
//        return ResponseEntity.ok(registeredUser);
//    }
//
//    @GetMapping("/me")
//    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
//        String email = authentication.getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        UserResponse response = new UserResponse(
//                user.getEmail(),
//                user.getUsername(),
//                user.getRoles().getName().name(),
//                user.getLastLoginAt(),
//                user.getAccount()
//        );
//        return ResponseEntity.ok(response);
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @PutMapping("/{email}")
//    public ResponseEntity<UserResponse> updateUser(@PathVariable String email, @RequestBody RegisterRequest updatedRequest) {
//        UserResponse updatedUser = userService.updateUser(email, updatedRequest);
//        return ResponseEntity.ok(updatedUser);
//    }
//
//    @PreAuthorize("hasRole('CUSTOMER')")
//    @GetMapping("/{email}")
//    public Set<Accounts> getUserAccounts(@PathVariable String email) {
//        return userService.getUserAccounts(email);
//    }
//}

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
    public ResponseEntity<ApiResponse<List<RegisterRequest>>> getAllUsers() {
        List<RegisterRequest> users = userService.getAllUsers();
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
    @PutMapping("/{email}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable String email, @RequestBody RegisterRequest updatedRequest) {
        UserResponse updatedUser = userService.updateUser(email, updatedRequest);
        ApiResponse<UserResponse> response = new ApiResponse<>(200, "User updated successfully", updatedUser);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<Set<Accounts>>> getUserAccounts(@PathVariable String email) {
        Set<Accounts> accounts = userService.getUserAccounts(email);
        ApiResponse<Set<Accounts>> response = new ApiResponse<>(200, "User accounts fetched successfully", accounts);
        return ResponseEntity.ok(response);
    }
}
