package com.example.cloudBalance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class EC2Instance {
    @Id
    @Column(name = "resource_id")
    private String instanceId;
    private String resourceName;
    private String region;
    private String status;

    public EC2Instance(String instanceId, String resourceName, String region, String status) {
        this.instanceId = instanceId;
        this.resourceName = resourceName;
        this.region = region;
        this.status = status;
    }

    public EC2Instance() {
    }

}
