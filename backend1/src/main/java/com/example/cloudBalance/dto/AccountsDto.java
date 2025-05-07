package com.example.cloudBalance.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AccountsDto {
    private String arnNumber;
    private String accountName;
    private String accountNumber;

    public AccountsDto(String arnNumber, String accountName, String accountNumber) {
        this.arnNumber = arnNumber;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
    }
}
