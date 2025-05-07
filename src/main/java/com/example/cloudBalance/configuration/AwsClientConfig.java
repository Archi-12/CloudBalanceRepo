//package com.example.cloudBalance.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
//import software.amazon.awssdk.services.ec2.Ec2Client;
//import software.amazon.awssdk.services.rds.RdsClient;
//import software.amazon.awssdk.services.sts.StsClient;
//import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
//import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
//import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
//
//@Configuration
//public class AwsClientConfig {
//
//    private final String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
//    private final String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
//    private final Region region = Region.AP_SOUTH_1;
//
//    @Bean
//    public StsClient stsClient() {
//        return StsClient.builder()
//                .credentialsProvider(StaticCredentialsProvider.create(
//                        AwsBasicCredentials.create(accessKey, secretKey)))
//                .region(region)
//                .build();
//    }
//
//
//    public Ec2Client ec2ClientForAccount(String accountNumber) {
//          // Replace with your logic to retrieve the account number
//        String roleArn = "arn:aws:iam::" + accountNumber + ":role/EC2ReadOnlyRole";
//
//        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
//                .roleArn(roleArn)
//                .roleSessionName("cloud-balance-session-" + accountNumber)
//                .build();
//
//        AssumeRoleResponse response = stsClient().assumeRole(assumeRoleRequest);
//
//        AwsSessionCredentials tempCreds = AwsSessionCredentials.create(
//                response.credentials().accessKeyId(),
//                response.credentials().secretAccessKey(),
//                response.credentials().sessionToken()
//        );
//
//        return Ec2Client.builder()
//                .credentialsProvider(StaticCredentialsProvider.create(tempCreds))
//                .region(region)
//                .build();
//    }
//
//
//    public AutoScalingClient autoScalingClient(String accountNumber) {
//               String roleArn = "arn:aws:iam::" + accountNumber + ":role/EC2ReadOnlyRole";
//
//        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
//                .roleArn(roleArn)
//                .roleSessionName("cloud-balance-session-" + accountNumber)
//                .build();
//
//        try {
//            AssumeRoleResponse response = stsClient().assumeRole(assumeRoleRequest);
//            AwsSessionCredentials tempCreds = AwsSessionCredentials.create(
//                    response.credentials().accessKeyId(),
//                    response.credentials().secretAccessKey(),
//                    response.credentials().sessionToken()
//            );
//            return AutoScalingClient.builder()
//                    .credentialsProvider(StaticCredentialsProvider.create(tempCreds))
//                    .region(region)
//                    .build();
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to assume role: " + e.getMessage(), e);
//        }
//    }
//
//
//    public RdsClient rdsClient(String accountNumber) {
//          // Replace with your logic to retrieve the account number
//        String roleArn = "arn:aws:iam::" + accountNumber + ":role/EC2ReadOnlyRole";
//
//        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
//                .roleArn(roleArn)
//                .roleSessionName("cloud-balance-session-" + accountNumber)
//                .build();
//
//        AssumeRoleResponse response = stsClient().assumeRole(assumeRoleRequest);
//        AwsSessionCredentials tempCreds = AwsSessionCredentials.create(
//                response.credentials().accessKeyId(),
//                response.credentials().secretAccessKey(),
//                response.credentials().sessionToken()
//        );
//
//        return RdsClient.builder()
//                .credentialsProvider(StaticCredentialsProvider.create(tempCreds))
//                .region(region)
//                .build();
//    }
//}
package com.example.cloudBalance.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.StsException;

import java.util.UUID;

@Configuration
public class AwsClientConfig {

    private final String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
    private final String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
    private final Region region = Region.AP_SOUTH_1;
    private final String roleName = "EC2ReadOnlyRole"; // centralized

    @Bean
    public StsClient stsClient() {
        return StsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .region(region)
                .build();
    }

    private AwsSessionCredentials assumeRole(String accountId) {
        String roleArn = "arn:aws:iam::" + accountId + ":role/" + roleName;

        try {
            AssumeRoleRequest request = AssumeRoleRequest.builder()
                    .roleArn(roleArn)
                    .roleSessionName("cloud-balance-session-" + UUID.randomUUID())
                    .build();

            AssumeRoleResponse response = stsClient().assumeRole(request);

            if (response.credentials() == null) {
                throw new RuntimeException("AssumeRole succeeded but returned null credentials for account: " + accountId);
            }

            return AwsSessionCredentials.create(
                    response.credentials().accessKeyId(),
                    response.credentials().secretAccessKey(),
                    response.credentials().sessionToken()
            );

        } catch (StsException e) {
            throw new RuntimeException("STS AssumeRole failed for account " + accountId + ": " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    public Ec2Client ec2ClientForAccount(String accountId) {
        AwsSessionCredentials tempCreds = assumeRole(accountId);

        return Ec2Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(tempCreds))
                .region(region)
                .build();
    }

    public AutoScalingClient autoScalingClient(String accountId) {
        AwsSessionCredentials tempCreds = assumeRole(accountId);

        return AutoScalingClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(tempCreds))
                .region(region)
                .build();
    }

    public RdsClient rdsClient(String accountId) {
        AwsSessionCredentials tempCreds = assumeRole(accountId);

        return RdsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(tempCreds))
                .region(region)
                .build();
    }
}
