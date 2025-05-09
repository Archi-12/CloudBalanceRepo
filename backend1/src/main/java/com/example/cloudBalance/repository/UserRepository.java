package com.example.cloudBalance.repository;

import com.example.cloudBalance.entity.ERole;
import com.example.cloudBalance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    Optional<List<User>> findByRoles(@Param("roleName") ERole role);
    boolean existsByEmail(String email);
}
