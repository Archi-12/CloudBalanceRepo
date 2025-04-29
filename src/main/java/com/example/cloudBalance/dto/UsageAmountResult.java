package com.example.cloudBalance.dto;

import lombok.Data;

@Data
public class UsageAmountResult {
    private String groupByValue;
    private double totalUsageAmount;
    private String monthYear;

}
