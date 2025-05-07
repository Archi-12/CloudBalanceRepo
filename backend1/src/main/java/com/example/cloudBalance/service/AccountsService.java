package com.example.cloudBalance.service;

import com.example.cloudBalance.dto.AccountsDto;
import com.example.cloudBalance.entity.Accounts;
import com.example.cloudBalance.exception.ConflictException;
import com.example.cloudBalance.repository.AccountsRepository;
import com.example.cloudBalance.mapper.AccountsMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class AccountsService {

        private final AccountsRepository accountsRepository;

        public AccountsService(AccountsRepository accountsRepository) {
            this.accountsRepository = accountsRepository;
        }

        public AccountsDto saveAccount(AccountsDto accountsDto) {
            if (accountsRepository.existsById(accountsDto.getAccountNumber())){
                throw new ConflictException("Account already exists");
            }
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
