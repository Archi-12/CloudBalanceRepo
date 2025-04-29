//package com.example.cloudBalance.Dto;
//
//
//import com.example.cloudBalance.Entity.Accounts;
//import com.example.cloudBalance.Entity.Role;
//import lombok.Data;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.Set;
//
//@Getter
//@Setter
//@Data
//public class RegisterRequest {
//    private String email;
//    private String password;
//    private Role roles;
//    private String username;
//    private Set<Accounts> accounts;
//
//}

package com.example.cloudBalance.dto;

import com.example.cloudBalance.entity.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role roles;
    private String username;
    private LocalDateTime lastLoginAt;
    private Set<String> accountNumbers; // Changed to account numbers for mapping

}
