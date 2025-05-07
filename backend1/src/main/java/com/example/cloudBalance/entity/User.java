package com.example.cloudBalance.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name="user")
@Getter
@Setter
public class User {

    @Id
    private Long id;

    @Column(name = "email",nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "Role_id")
    private Role roles;

    @Column(name = "username")
    private String username;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;


    @ManyToMany
    @JoinTable(
            name = "user_accounts",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "account_number")
    )
    private Set<Accounts> account;

    public User() {
    }

   public User (Long id,String email, String password, Role roles, String username, Set<Accounts> account) {
        this.id=id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.username = username;
        this.account = account;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", username='" + username + '\'' +
                ", account=" + account +
                '}';
    }
}
