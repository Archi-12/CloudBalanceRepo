package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.RdsDto;
import com.example.cloudBalance.dto.ApiResponse;
import com.example.cloudBalance.service.RDSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rds")
public class RdsController {

    @Autowired
    private RDSService rdsService;

    @GetMapping("/instances/{accountNumber}")
    public ResponseEntity<ApiResponse<List<RdsDto>>> getDBInstances(@PathVariable String accountNumber) {
        try {
            List<RdsDto> dbInstances = rdsService.describeRdsInstances(accountNumber);

            if (dbInstances.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "No RDS instances found or access denied", dbInstances));
            }

            return ResponseEntity.ok(new ApiResponse<>(200, "RDS instances fetched successfully", dbInstances));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access denied or invalid role ARN", null));
        }
    }
}

