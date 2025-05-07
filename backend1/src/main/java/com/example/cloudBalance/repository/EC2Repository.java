package com.example.cloudBalance.repository;

import com.example.cloudBalance.entity.EC2Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface EC2Repository extends JpaRepository<EC2Instance, String> {
    }

