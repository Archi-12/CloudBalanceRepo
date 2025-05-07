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

        public AuthResponse(String token, String email) {
            this.token = token;
            this.email = email;

        }
}


