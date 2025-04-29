package com.example.cloudBalance.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;

@Configuration
public class AwsClientConfig {

    private final String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
    private final String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
    private final Region region = Region.AP_SOUTH_1;

    @Bean
    public StsClient stsClient() {
        return StsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .region(region)
                .build();
    }

    public Ec2Client ec2ClientForAccount(String accountNumber) {
        String roleArn = "arn:aws:iam::" + accountNumber + ":role/EC2ReadOnlyRole";

        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
                .roleArn(roleArn)
                .roleSessionName("cloud-balance-session-" + accountNumber)
                .build();

        AssumeRoleResponse response = stsClient().assumeRole(assumeRoleRequest);

        AwsSessionCredentials tempCreds = AwsSessionCredentials.create(
                response.credentials().accessKeyId(),
                response.credentials().secretAccessKey(),
                response.credentials().sessionToken()
        );

        return Ec2Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(tempCreds))
                .region(region)
                .build();
    }

    @Bean
    public AutoScalingClient autoScalingClient() {
        return AutoScalingClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .region(region)
                .build();
    }

    @Bean
    public RdsClient rdsClient() {
        return RdsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .region(region)
                .build();
    }
}
