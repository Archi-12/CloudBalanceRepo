//package com.example.cloudBalance.userManagement;
//
//import com.example.cloudBalance.Dto.RegisterRequest;
//import com.example.cloudBalance.Entity.User;
//
//public class UserMapper {
//    public static RegisterRequest toDto(User user) {
//        if (user == null) {
//            return null;
//        }
//        RegisterRequest registerRequest = new RegisterRequest();
//        registerRequest.setEmail(user.getEmail());
//        registerRequest.setPassword(user.getPassword());
//        registerRequest.setRoles(user.getRoles());
//        registerRequest.setUsername(user.getUsername());
//        return registerRequest;
//    }
//
//    public static User toEntity(RegisterRequest registerRequest) {
//        if (registerRequest == null) {
//            return null;
//        }
//        User user = new User();
//        user.setEmail(registerRequest.getEmail());
//        user.setPassword(registerRequest.getPassword());
//        user.setRoles(registerRequest.getRoles());
//        user.setUsername(registerRequest.getUsername());
//        return user;
//    }
//
//}

package com.example.cloudBalance.mapper;

import com.example.cloudBalance.dto.RegisterRequest;
import com.example.cloudBalance.entity.Accounts;
import com.example.cloudBalance.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static RegisterRequest toDto(User user) {
        if (user == null) {
            return null;
        }
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(user.getEmail());
        registerRequest.setPassword(user.getPassword());
        registerRequest.setRoles(user.getRoles());
        registerRequest.setUsername(user.getUsername());
        registerRequest.setLastLoginAt(user.getLastLoginAt());

        if (user.getAccount() != null) {
            Set<String> accountNumbers = user.getAccount().stream()
                    .map(Accounts::getAccountNumber)
                    .collect(Collectors.toSet());
            registerRequest.setAccountNumbers(accountNumbers);
        }

        return registerRequest;
    }

    public static User toEntity(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            return null;
        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRoles(registerRequest.getRoles());
        user.setUsername(registerRequest.getUsername());

        return user;
    }
}

