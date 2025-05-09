package com.example.cloudBalance.service;

import com.example.cloudBalance.configuration.AwsClientConfig;
import com.example.cloudBalance.dto.EC2InstanceDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.ArrayList;
import java.util.List;
@Service
public class EC2Service {

    private final AwsClientConfig awsClientConfig;

    @Value("${aws.region}")
    private String awsRegion;

    public EC2Service(AwsClientConfig awsClientConfig) {
        this.awsClientConfig = awsClientConfig;
    }

    private static final Logger logger = LogManager.getLogger(EC2Service.class);

    public List<EC2InstanceDTO> describeEC2Instances(String accountId) {
        List<EC2InstanceDTO> dtoList = new ArrayList<>();
        String nextToken = null;

        Ec2Client ec2Client = awsClientConfig.ec2ClientForAccount(accountId); // 🟢 dynamic client

        try {
            do {
                DescribeInstancesRequest request = DescribeInstancesRequest.builder()
                        .maxResults(10)
                        .nextToken(nextToken)
                        .build();

                DescribeInstancesResponse response = ec2Client.describeInstances(request);

                for (Reservation reservation : response.reservations()) {
                    for (Instance instance : reservation.instances()) {
                        EC2InstanceDTO dto = new EC2InstanceDTO(
                                instance.instanceId(),
                                instance.tags().stream()
                                        .filter(tag -> "Name".equals(tag.key()))
                                        .map(Tag::value)
                                        .findFirst()
                                        .orElse("Unnamed"),
                                awsRegion,
                                instance.state().nameAsString()
                        );
                        dtoList.add(dto);
                    }
                }

                nextToken = response.nextToken();

            } while (nextToken != null);

        } catch (Ec2Exception e) {
            logger.error("Failed to fetch EC2 instances: {}", e.awsErrorDetails().errorMessage());
        }

        return dtoList;
    }
}


