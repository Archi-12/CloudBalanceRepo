package com.example.cloudBalance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;


import java.util.Date;

@Entity
@Table(name = "blacklisted_tokens")
@Getter
@Setter
public class BlackListedToken {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String token;

        private Date expiration;

        public BlackListedToken() {}

        public BlackListedToken(String token, Date expiration) {
            this.token = token;
            this.expiration = expiration;
        }
    }

