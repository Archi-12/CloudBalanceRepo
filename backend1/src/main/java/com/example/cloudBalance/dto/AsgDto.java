package com.example.cloudBalance.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class AsgDto {

        private String instanceIds;
        private String resourceName;
        private String region;
        private Integer desiredCapacity;
        private Integer minSize;
        private Integer maxSize;
        private String status;


        public AsgDto(String instanceIds,String resourceName, String region, Integer desiredCapacity,
                      Integer minSize, Integer maxSize, String status)
        {
            this.instanceIds = instanceIds;
            this.resourceName = resourceName;
            this.region = region;
            this.desiredCapacity = desiredCapacity;
            this.minSize = minSize;
            this.maxSize = maxSize;
            this.status = status;

        }
    }


