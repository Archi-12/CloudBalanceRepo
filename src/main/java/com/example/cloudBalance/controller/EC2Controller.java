////package com.example.cloudBalance.controller;
////
////import com.example.cloudBalance.dto.EC2InstanceDTO;
////import com.example.cloudBalance.service.EC2Service;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.List;
////
////@RestController
////@RequestMapping("/api/ec2")
////public class EC2Controller {
////
////    private final EC2Service ec2Service;
////
////    @Autowired
////    public EC2Controller(EC2Service ec2Service) {
////        this.ec2Service = ec2Service;
////    }
////
////    @GetMapping("/instances/{accountNumber}")
////    public List<EC2InstanceDTO> getEC2InstancesByAccount(@PathVariable String accountNumber) {
////        return ec2Service.describeEC2Instances(accountNumber);
////    }
////}
//package com.example.cloudBalance.controller;
//
//import com.example.cloudBalance.dto.EC2InstanceDTO;
//import com.example.cloudBalance.repository.UserRepository;
//import com.example.cloudBalance.service.EC2Service;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/ec2")
//public class EC2Controller {
//
//    private final EC2Service ec2Service;
//    private final UserRepository userRepository;
//
//    @Autowired
//    public EC2Controller(EC2Service ec2Service, UserRepository userRepository) {
//        this.ec2Service = ec2Service;
//        this.userRepository = userRepository;
//    }
//
//    @GetMapping("/instances/{accountNumber}")
//    public List<EC2InstanceDTO> getEC2InstancesByAccount(
//            @PathVariable String accountNumber) {
//        return ec2Service.describeEC2Instances(accountNumber);
//    }
//}

package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.EC2InstanceDTO;
import com.example.cloudBalance.repository.UserRepository;
import com.example.cloudBalance.dto.ApiResponse;
import com.example.cloudBalance.service.EC2Service;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ApiResponse<List<EC2InstanceDTO>>> getEC2InstancesByAccount(
            @PathVariable String accountNumber) {

        List<EC2InstanceDTO> instances = ec2Service.describeEC2Instances(accountNumber);

        ApiResponse<List<EC2InstanceDTO>> response = new ApiResponse<>(200, "EC2 instances fetched successfully", instances);
        return ResponseEntity.ok(response);
    }
}

