package com.example.cloudBalance.mapper;

import com.example.cloudBalance.dto.AccountsDto;
import com.example.cloudBalance.entity.Accounts;

public class AccountsMapper {

        public static AccountsDto toDto(Accounts accounts) {
            return new AccountsDto(accounts.getArnNumber(), accounts.getAccountName(), accounts.getAccountNumber());
        }

        public static Accounts toEntity(AccountsDto accountsDto) {
            return new Accounts(accountsDto.getArnNumber(), accountsDto.getAccountName(), accountsDto.getAccountNumber());
        }

    }


