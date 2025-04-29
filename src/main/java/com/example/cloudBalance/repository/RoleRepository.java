package com.example.cloudBalance.repository;

import com.example.cloudBalance.entity.ERole;
import com.example.cloudBalance.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
    ;
}
