package com.example.cloudBalance.repository;

import com.example.cloudBalance.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, String> {
    @Override
    boolean existsById(String accountNumber);
}
