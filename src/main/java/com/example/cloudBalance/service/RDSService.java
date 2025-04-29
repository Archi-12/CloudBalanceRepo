package com.example.cloudBalance.service;

import com.example.cloudBalance.dto.RdsDto;
import com.example.cloudBalance.repository.AccountRepository;
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

    private static final Logger logger = LogManager.getLogger(RDSService.class);

    private final AccountRepository accountRepository;
    private final RdsClient rdsClient;

    @Value("${aws.region}")
    private String awsRegion;

    public RDSService(AccountRepository accountRepository,RdsClient rdsClient) {
        this.accountRepository = accountRepository;
        this.rdsClient = rdsClient;
    }

    public List<RdsDto> describeDBInstances(String accountId) {
        List<RdsDto> dtoList = new ArrayList<>();

        try {
            DescribeDbInstancesRequest request = DescribeDbInstancesRequest.builder().build();
            DescribeDbInstancesResponse response = rdsClient.describeDBInstances(request);

            for (DBInstance dbInstance : response.dbInstances()) {
                if (accountId == null || dbInstance.dbInstanceArn().contains(accountId)) {
                    RdsDto dto = new RdsDto();
                    dto.setInstanceId(dbInstance.dbInstanceArn());
                    dto.setResourceName(dbInstance.dbInstanceIdentifier());
                    dto.setEngine(dbInstance.engine());
                    dto.setRegion(awsRegion);
                    dto.setStatus(dbInstance.dbInstanceStatus());
                    dtoList.add(dto);
                }
            }

        } catch (RdsException e) {
            logger.error("Error fetching RDS Instances: ", e);
        }
        return dtoList;
    }
}
