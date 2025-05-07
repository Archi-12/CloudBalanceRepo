//package com.example.cloudBalance.service;
//
//import com.example.cloudBalance.configuration.AwsClientConfig;
//import com.example.cloudBalance.dto.RdsDto;
//import com.example.cloudBalance.repository.AccountRepository;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.services.rds.RdsClient;
//import software.amazon.awssdk.services.rds.model.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class RDSService {
//
//    private static final Logger logger = LogManager.getLogger(RDSService.class);
//
//    private final AwsClientConfig awsClientConfig;
//    private final AccountRepository accountRepository;
//
//    @Value("${aws.region}")
//    private String awsRegion;
//
//    public RDSService(AwsClientConfig awsClientConfig, AccountRepository accountRepository) {
//        this.awsClientConfig = awsClientConfig;
//        this.accountRepository = accountRepository;
//    }
//
//    public List<RdsDto> describeDBInstances(String accountId) {
//        List<RdsDto> dtoList = new ArrayList<>();
//
//        try (RdsClient rdsClient = awsClientConfig.rdsClient(accountId)) {
//            DescribeDbInstancesRequest request = DescribeDbInstancesRequest.builder().build();
//            DescribeDbInstancesResponse response = rdsClient.describeDBInstances(request);
//
//            for (DBInstance dbInstance : response.dbInstances()) {
//                if (accountId == null || dbInstance.dbInstanceArn().contains(accountId)) {
//                    RdsDto dto = new RdsDto();
//                    dto.setInstanceId(dbInstance.dbInstanceArn());
//                    dto.setResourceName(dbInstance.dbInstanceIdentifier());
//                    dto.setEngine(dbInstance.engine());
//                    dto.setRegion(awsRegion);
//                    dto.setStatus(dbInstance.dbInstanceStatus());
//                    dtoList.add(dto);
//                }
//            }
//
//        } catch (RdsException e) {
//            logger.error("Error fetching RDS Instances: ", e);
//        } catch (Exception e) {
//            logger.error("Unexpected error occurred: ", e);
//        }
//
//        return dtoList;
//    }
//}

package com.example.cloudBalance.service;

import com.example.cloudBalance.configuration.AwsClientConfig;
import com.example.cloudBalance.dto.RdsDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class RDSService {

    private final AwsClientConfig awsClientConfig;

    @Value("${aws.region}")
    private String awsRegion;

    private static final Logger logger = LogManager.getLogger(RDSService.class);

    public RDSService(AwsClientConfig awsClientConfig) {
        this.awsClientConfig = awsClientConfig;
    }

    public List<RdsDto> describeRdsInstances(String accountId) {
        List<RdsDto> dtoList = new ArrayList<>();
        RdsClient rdsClient;

        try {
            rdsClient = awsClientConfig.rdsClient(accountId);
        } catch (Exception e) {
            logger.error("Failed to create RdsClient: {}", e.getMessage());
            return dtoList;
        }

        try {
            DescribeDbInstancesResponse response = rdsClient.describeDBInstances();

            for (DBInstance db : response.dbInstances()) {
                RdsDto dto = new RdsDto(
                        db.dbInstanceIdentifier(),
                        db.dbName() != null ? db.dbName() : "Unnamed",
                        db.engine(),
                        awsRegion,
                        db.dbInstanceStatus()
                );
                dtoList.add(dto);
            }

        } catch (RdsException e) {
            logger.error("Failed to fetch RDS instances: {}", e.awsErrorDetails().errorMessage());
        }

        return dtoList;
    }
}


