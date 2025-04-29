package com.example.cloudBalance.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
