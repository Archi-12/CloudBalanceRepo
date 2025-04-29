package com.example.cloudBalance.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Setter
@Getter
@Table(name = "accounts")
public class Accounts {

    @Id
    @Column(name="account_number")
    private String accountNumber;

    @Column(name="account_name")
    private String accountName;

    @Column(name="arn_number")
    private String arnNumber;


    public Accounts(String arnNumber, String accountName, String accountNumber) {
        this.arnNumber = arnNumber;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
    }
    public Accounts() {
    }

}