package com.example.cloudBalance.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AuthResponse {

        private String token;
        private String email;
        private String role;
        private String username;

        public AuthResponse(String token, String email, String role, String username) {
            this.token = token;
            this.email = email;
            this.role = role;
            this.username = username;
        }

}


