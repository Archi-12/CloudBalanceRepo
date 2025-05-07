//package com.example.cloudBalance.service;
//
//import com.example.cloudBalance.configuration.AwsClientConfig;
//import com.example.cloudBalance.dto.AsgDto;
//import com.example.cloudBalance.repository.AccountRepository;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
//import software.amazon.awssdk.services.autoscaling.model.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class ASGService {
//
//    private static final Logger logger = LogManager.getLogger(ASGService.class);
//
//    private final AwsClientConfig awsClientConfig;
//    private final AccountRepository accountRepository;
//
//    @Value("${aws.region}")
//    private String awsRegion;
//
//    public ASGService(AwsClientConfig awsClientConfig, AccountRepository accountRepository) {
//        this.awsClientConfig = awsClientConfig;
//        this.accountRepository = accountRepository;
//    }
//
//    public List<AsgDto> describeAutoScalingGroups(String accountId) {
//        List<AsgDto> dtoList = new ArrayList<>();
//
//        try (AutoScalingClient autoScalingClient = awsClientConfig.autoScalingClient(accountId)) {
//            DescribeAutoScalingGroupsRequest request = DescribeAutoScalingGroupsRequest.builder().build();
//            DescribeAutoScalingGroupsResponse response = autoScalingClient.describeAutoScalingGroups(request);
//
//            for (AutoScalingGroup asg : response.autoScalingGroups()) {
//                // Account-based filtering
//                if (accountId == null || asg.autoScalingGroupARN().contains(accountId)) {
//                    AsgDto dto = new AsgDto(
//                            asg.autoScalingGroupARN(),
//                            asg.autoScalingGroupName(),
//                            awsRegion,
//                            asg.desiredCapacity(),
//                            asg.minSize(),
//                            asg.maxSize(),
//                            asg.status() != null ? asg.status() : "N/A"
//                    );
//                    dtoList.add(dto);
//                }
//            }
//        } catch (AutoScalingException e) {
//            logger.error("Error fetching Auto Scaling Groups: ", e);
//        } catch (Exception e) {
//            logger.error("Unexpected error occurred: ", e);
//        }
//
//        return dtoList;
//    }
//}

package com.example.cloudBalance.service;

import com.example.cloudBalance.configuration.AwsClientConfig;
import com.example.cloudBalance.dto.AsgDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.autoscaling.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class ASGService {

    private final AwsClientConfig awsClientConfig;

    @Value("${aws.region}")
    private String awsRegion;

    private static final Logger logger = LogManager.getLogger(ASGService.class);

    public ASGService(AwsClientConfig awsClientConfig) {
        this.awsClientConfig = awsClientConfig;
    }

    public List<AsgDto> describeAutoScalingGroups(String accountId) {
        List<AsgDto> dtoList = new ArrayList<>();
        AutoScalingClient asgClient;

        try {
            asgClient = awsClientConfig.autoScalingClient(accountId);
        } catch (Exception e) {
            logger.error("Failed to assume role for ASG: {}", e.getMessage());
            throw new RuntimeException("Unauthorized: AssumeRole failed", e); // 🔴 Propagate for controller to catch
        }

        try {
            DescribeAutoScalingGroupsResponse response = asgClient.describeAutoScalingGroups();

            for (AutoScalingGroup group : response.autoScalingGroups()) {
                String instanceIds = group.instances().stream()
                        .map(Instance::instanceId)
                        .reduce((id1, id2) -> id1 + ", " + id2)
                        .orElse("None");

                AsgDto dto = new AsgDto(
                        instanceIds,
                        group.autoScalingGroupName(),
                        awsRegion,
                        group.desiredCapacity(),
                        group.minSize(),
                        group.maxSize(),
                        group.status() != null ? group.status() : "N/A"
                );

                dtoList.add(dto);
            }

        } catch (AutoScalingException e) {
            logger.error("Failed to fetch Auto Scaling Groups: {}", e.awsErrorDetails().errorMessage());
        }

        return dtoList;
    }
}
