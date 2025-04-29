package com.example.cloudBalance.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EC2InstanceDTO {
    private String instanceId;
    private String resourceName;
    private String region;
    private String status;

    // Constructors
    public EC2InstanceDTO(String instanceId, String resourceName, String region, String status) {
        this.instanceId = instanceId;
        this.resourceName = resourceName;
        this.region = region;
        this.status = status;
    }

    // Getters and Setters
}

