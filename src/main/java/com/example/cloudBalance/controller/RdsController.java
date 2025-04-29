//package com.example.cloudBalance.controller;
//
//
//import com.example.cloudBalance.dto.EC2InstanceDTO;
//import com.example.cloudBalance.dto.RdsDto;
//import com.example.cloudBalance.service.RDSService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/rds")
//public class RdsController {
//    @Autowired
//    private RDSService rdsService;
//
//    @GetMapping("/instances/{accountNumber}")
//    public List<RdsDto> getDBInstances(@PathVariable String accountNumber) {
//        return rdsService.describeDBInstances(accountNumber);
//    }
//}

package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.RdsDto;
import com.example.cloudBalance.dto.ApiResponse;
import com.example.cloudBalance.service.RDSService;
import org.springframework.beans.factory.annotation.Autowired;
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

        List<RdsDto> dbInstances = rdsService.describeDBInstances(accountNumber);

        ApiResponse<List<RdsDto>> response = new ApiResponse<>(200, "RDS instances fetched successfully", dbInstances);
        return ResponseEntity.ok(response);
    }
}
