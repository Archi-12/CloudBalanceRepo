package com.example.cloudBalance.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RdsDto {
    private String instanceId;
    private String resourceName;
    private String engine;
    private String region;
    private String status;

    public RdsDto() {
    }

    public RdsDto(String instanceId, String resourceName, String engine, String region, String status) {
        this.instanceId = instanceId;
        this.resourceName = resourceName;
        this.engine=engine;
        this.region = region;
        this.status = status;
    }

        public RdsDto(String instanceId) {
            this.instanceId = instanceId;
        }




    }

