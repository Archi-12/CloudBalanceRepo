package com.example.cloudBalance.service;

import com.example.cloudBalance.authComp.JwtService;
import com.example.cloudBalance.dto.RegisterRequest;
import com.example.cloudBalance.dto.UserResponse;
import com.example.cloudBalance.entity.Accounts;
import com.example.cloudBalance.entity.Role;
import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.exception.ConflictException;
import com.example.cloudBalance.exception.ResourceNotFound;
import com.example.cloudBalance.exception.UnauthorizedException;
import com.example.cloudBalance.mapper.UserMapper;
import com.example.cloudBalance.repository.AccountRepository;
import com.example.cloudBalance.repository.RoleRepository;
import com.example.cloudBalance.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtService jwtTokenProvider;

    public List<RegisterRequest> getAllUsers(Authentication authentication) {
        String currentUserEmail = authentication.getName(); // Get the logged-in user's email
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> !user.getEmail().equals(currentUserEmail)) // Exclude the current user
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse saveUser(RegisterRequest registerRequest) {
        // Fetch role from DB using ERole value
        Role role = roleRepository.findByName(registerRequest.getRoles().getName())
                .orElseThrow(() -> new ResourceNotFound("Role not found"));

        // Check if the email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        user.setId(registerRequest.getId());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setUsername(registerRequest.getUsername());
        user.setRoles(role);

        // Check if the role is "CUSTOMER" and populate account details
        if (role.getName().name().equals("CUSTOMER") && registerRequest.getAccountNumbers() != null) {
            Set<Accounts> accounts = accountRepository.findByAccountNumberIn(registerRequest.getAccountNumbers());
            if (accounts.size() != registerRequest.getAccountNumbers().size()) {
                throw new UnauthorizedException("One or more account numbers are invalid.");
            }

            user.setAccount(accounts);
        }

        User savedUser = userRepository.save(user);

        UserResponse dto = new UserResponse();
        dto.setId(savedUser.getId());
        dto.setEmail(savedUser.getEmail());
        dto.setUsername(savedUser.getUsername());
        dto.setRoles(savedUser.getRoles().getName().toString());

        if (savedUser.getAccount() != null) {
            Set<Accounts> accountNames = savedUser.getAccount();
            dto.setAccounts(accountNames);
        }
        return dto;
    }

    @Transactional
    public UserResponse updateUser(Long id, RegisterRequest updatedRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User not found with id: " + id));

        // Update username
        existingUser.setUsername(updatedRequest.getUsername());

        // Update password only if provided
        if (updatedRequest.getPassword() != null && !updatedRequest.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedRequest.getPassword()));
        }

        // Update role if changed
        if (updatedRequest.getRoles() != null) {
            Role newRole = roleRepository.findByName(updatedRequest.getRoles().getName())
                    .orElseThrow(() -> new ResourceNotFound("Role not found"));
            existingUser.setRoles(newRole);
        }

        // Update accounts if role is CUSTOMER and accountNumbers are provided
        if (updatedRequest.getRoles() != null &&
                updatedRequest.getRoles().getName().name().equals("CUSTOMER") &&
                updatedRequest.getAccountNumbers() != null) {

            Set<Accounts> accounts = accountRepository.findByAccountNumberIn(updatedRequest.getAccountNumbers());
            if (accounts.size() != updatedRequest.getAccountNumbers().size()) {
                throw new UnauthorizedException("One or more account numbers are invalid.");
            }

            existingUser.setAccount(accounts);
        }

        User savedUser = userRepository.save(existingUser);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setUsername(savedUser.getUsername());
        response.setRoles(savedUser.getRoles().getName().toString());
        response.setAccounts(savedUser.getAccount());

        return response;
    }

    public Set<Accounts> getUserAccounts(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User not found with id: " + id));
        return user.getAccount();
    }

    public String switchToUser(Long id, Authentication authentication) {
        // Validate the customer exists
        User customer = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Customer not found"));
        if (!customer.getRoles().getName().equals("CUSTOMER")) {
            throw new UnauthorizedException("Cannot switch to a non-customer user");
        }
        return jwtTokenProvider.generateToken(customer.getEmail(), customer.getRoles().getName().name());
    }

}



