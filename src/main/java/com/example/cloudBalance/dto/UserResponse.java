package com.example.cloudBalance.dto;

import com.example.cloudBalance.entity.Accounts;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Getter
@Setter
public class UserResponse {
    private String email;
    private String username;
    private String roles;
    private LocalDateTime lastLoginAt;
    private Set<Accounts> accounts;

    public UserResponse() {}

    public UserResponse(String email, String username, String roleName,LocalDateTime lastLoginAt, Set<Accounts> accounts) {
        this.email = email;
        this.username = username;
        this.roles = roleName;
        this.accounts = accounts;
        this.lastLoginAt = lastLoginAt;
    }

    public UserResponse(Set<Accounts> accounts) {
        this.accounts = accounts;
    }
}
