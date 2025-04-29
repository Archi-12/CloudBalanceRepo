package com.example.cloudBalance.controller;

import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

//    @GetMapping("/all")
//    public String allAccess() {
//        return "Public Content.";
//    }
//
//    @GetMapping("/user")
//    @PreAuthorize("hasRole('READ_ONLY') or hasRole('CUSTOMER') or hasRole('ADMIN')")
//    public String readOnlyAccess() {
//        return "Read Only Content.";
//    }
//
//    @GetMapping("/mod")
//    @PreAuthorize("hasRole('CUSTOMER')")
//    public String CustomerAccess() {
//        return "Customer Board.";
//    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
