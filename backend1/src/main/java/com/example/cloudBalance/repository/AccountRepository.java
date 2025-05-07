//package com.example.cloudBalance.Repository;
//
//import com.example.cloudBalance.Entity.Accounts;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface AccountRepository extends JpaRepository<Accounts,String> {
//}

package com.example.cloudBalance.repository;

import com.example.cloudBalance.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AccountRepository extends JpaRepository<Accounts, String> {

    Set<Accounts> findByAccountNumberIn(Set<String> accountNumbers);

}
