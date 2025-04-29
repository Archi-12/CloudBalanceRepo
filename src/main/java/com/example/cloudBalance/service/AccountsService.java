package com.example.cloudBalance.service;

import com.example.cloudBalance.dto.AccountsDto;
import com.example.cloudBalance.entity.Accounts;
import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.repository.AccountRepository;
import com.example.cloudBalance.repository.AccountsRepository;
import com.example.cloudBalance.mapper.AccountsMapper;
import com.example.cloudBalance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class AccountsService {

        private final AccountsRepository accountsRepository;

        public AccountsService(AccountsRepository accountsRepository) {
            this.accountsRepository = accountsRepository;
        }

        public AccountsDto saveAccount(AccountsDto accountsDto) {
            Accounts account = AccountsMapper.toEntity(accountsDto);
            Accounts saved = accountsRepository.save(account);
            return AccountsMapper.toDto(saved);
        }

        public List<AccountsDto> getAllAccounts() {
            return accountsRepository.findAll()
                    .stream()
                    .map(AccountsMapper::toDto)
                    .collect(Collectors.toList());
        }

}
