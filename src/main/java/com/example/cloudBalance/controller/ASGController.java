//package com.example.cloudBalance.controller;
//
//import com.example.cloudBalance.dto.AsgDto;
//import com.example.cloudBalance.dto.ApiResponse;
//import com.example.cloudBalance.service.ASGService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/asg")
//public class ASGController {
//
//    @Autowired
//    private ASGService asgService;
//
//    @GetMapping("/instances/{accountNumber}")
//    public ResponseEntity<ApiResponse<List<AsgDto>>> getASGInstances(@PathVariable String accountNumber) {
//        List<AsgDto> asgInstances = asgService.describeAutoScalingGroups(accountNumber);
//
//        ApiResponse<List<AsgDto>> response = new ApiResponse<>(
//                200,
//                "ASG instances fetched successfully",
//                asgInstances
//        );
//        return ResponseEntity.ok(response);
//    }
//}
package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.AsgDto;
import com.example.cloudBalance.dto.ApiResponse;
import com.example.cloudBalance.service.ASGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asg")
public class ASGController {

    @Autowired
    private ASGService asgService;

    @GetMapping("/instances/{accountNumber}")
    public ResponseEntity<ApiResponse<List<AsgDto>>> getASGInstances(@PathVariable String accountNumber) {
        try {
            List<AsgDto> asgInstances = asgService.describeAutoScalingGroups(accountNumber);

            if (asgInstances.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "No ASG instances found or access denied", asgInstances));
            }

            return ResponseEntity.ok(new ApiResponse<>(200, "ASG instances fetched successfully", asgInstances));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Access denied or invalid role ARN", null));
        }
    }
}
