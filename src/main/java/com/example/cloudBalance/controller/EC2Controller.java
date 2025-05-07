package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.EC2InstanceDTO;
import com.example.cloudBalance.repository.UserRepository;
import com.example.cloudBalance.dto.ApiResponse;
import com.example.cloudBalance.service.EC2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ec2")
public class EC2Controller {

    private final EC2Service ec2Service;
    private final UserRepository userRepository;

    @Autowired
    public EC2Controller(EC2Service ec2Service, UserRepository userRepository) {
        this.ec2Service = ec2Service;
        this.userRepository = userRepository;
    }

@GetMapping("/instances/{accountNumber}")
public ResponseEntity<ApiResponse<List<EC2InstanceDTO>>> getEC2InstancesByAccount(@PathVariable String accountNumber) {
    try {
        List<EC2InstanceDTO> instances = ec2Service.describeEC2Instances(accountNumber);

        if (instances.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "No ASG instances found or access denied", instances));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, "ASG instances fetched successfully", instances));

    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(403, "Access denied or invalid role ARN", null));
    }
}
}

