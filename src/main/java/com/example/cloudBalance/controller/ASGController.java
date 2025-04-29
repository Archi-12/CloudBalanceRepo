//package com.example.cloudBalance.controller;
//
//import com.example.cloudBalance.dto.AsgDto;
//import com.example.cloudBalance.service.ASGService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/asg")
//public class ASGController {
//    @Autowired
//    private ASGService asgService;
//
//    @GetMapping("/instances/{accountNumber}")
//    public List<AsgDto> getASGInstances(@PathVariable String accountNumber) {
//
//        return asgService.describeAutoScalingGroups(accountNumber);
//    }
//}
package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.AsgDto;
import com.example.cloudBalance.dto.ApiResponse;
import com.example.cloudBalance.service.ASGService;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<AsgDto> asgInstances = asgService.describeAutoScalingGroups(accountNumber);

        ApiResponse<List<AsgDto>> response = new ApiResponse<>(
                200,
                "ASG instances fetched successfully",
                asgInstances
        );
        return ResponseEntity.ok(response);
    }
}
