//package com.example.cloudBalance.controller;
//
//import com.example.cloudBalance.dto.AccountsDto;
//import com.example.cloudBalance.service.AccountsService;
//import com.example.cloudBalance.service.UserDetailsImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Set;
//
//
//@RestController
//@RequestMapping("/api/accounts")
//public class AccountsController {
//
//   @Autowired
//    private AccountsService accountsService;
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping
//    public ResponseEntity<AccountsDto> addAccount(@RequestBody AccountsDto accountDto) {
//        AccountsDto savedAccount = accountsService.saveAccount(accountDto);
//        return ResponseEntity.ok(savedAccount);
//    }
//
//    @PreAuthorize("hasAnyRole('ADMIN', 'READ_ONLY')")
//    @GetMapping
//    public ResponseEntity<List<AccountsDto>> getAllAccounts() {
//        List<AccountsDto> accounts = accountsService.getAllAccounts();
//        return ResponseEntity.ok(accounts);
//    }
//
//}

package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.AccountsDto;
import com.example.cloudBalance.dto.ApiResponse;
import com.example.cloudBalance.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {

    @Autowired
    private AccountsService accountsService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<AccountsDto>> addAccount(@RequestBody AccountsDto accountDto) {
        AccountsDto savedAccount = accountsService.saveAccount(accountDto);
        ApiResponse<AccountsDto> response = new ApiResponse<>(
                201,
                "Account created successfully",
                savedAccount
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'READ_ONLY')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountsDto>>> getAllAccounts() {
        List<AccountsDto> accounts = accountsService.getAllAccounts();

        ApiResponse<List<AccountsDto>> response = new ApiResponse<>(
                200,
                "Accounts fetched successfully",
                accounts
        );
        return ResponseEntity.ok(response);
    }
}

