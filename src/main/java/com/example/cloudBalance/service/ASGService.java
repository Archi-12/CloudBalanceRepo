package com.example.cloudBalance.service;

import com.example.cloudBalance.dto.AsgDto;
import com.example.cloudBalance.repository.AccountRepository;
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

    private static final Logger logger = LogManager.getLogger(ASGService.class);

    private final AutoScalingClient autoScalingClient;
    private final AccountRepository accountRepository;

    @Value("${aws.region}")
    private String awsRegion;

    public ASGService(AutoScalingClient autoScalingClient, AccountRepository accountRepository) {
        this.autoScalingClient = autoScalingClient;
        this.accountRepository = accountRepository;
    }

    public List<AsgDto> describeAutoScalingGroups(String accountId) {
        List<AsgDto> dtoList = new ArrayList<>();

        try {
            DescribeAutoScalingGroupsRequest request = DescribeAutoScalingGroupsRequest.builder().build();
            DescribeAutoScalingGroupsResponse response = autoScalingClient.describeAutoScalingGroups(request);

            for (AutoScalingGroup asg : response.autoScalingGroups()) {
                // Account-based filtering
                if (accountId == null || asg.autoScalingGroupARN().contains(accountId)) {
                    AsgDto dto = new AsgDto(
                            asg.autoScalingGroupARN(),
                            asg.autoScalingGroupName(),
                            awsRegion,
                            asg.desiredCapacity(),
                            asg.minSize(),
                            asg.maxSize(),
                            asg.status() != null ? asg.status() : "N/A"
                    );
                    dtoList.add(dto);
                }
            }
        } catch (AutoScalingException e) {
            logger.error("Error fetching Auto Scaling Groups: ", e);
        }

        return dtoList;
    }
}



