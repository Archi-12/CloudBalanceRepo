package com.example.cloudBalance.repository;

import com.example.cloudBalance.entity.SnowflakeColumns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface SnowflakeColumnRepository extends JpaRepository<SnowflakeColumns, Long> {

    @Query("SELECT s.displayName FROM SnowflakeColumns s")
    List<String> findAllDisplayNames();

    Optional<SnowflakeColumns> findByDisplayName(String displayName);
}