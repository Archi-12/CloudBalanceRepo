package com.example.cloudBalance.repository;

import com.example.cloudBalance.entity.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlackListedToken, Long> {
    boolean existsByToken(String jwt);
}

