package com.example.cloudBalance.authComp;

import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.repository.BlacklistedTokenRepository;
import com.example.cloudBalance.repository.UserRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EntityManagerFactory entityManagerFactory;


    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository,
                         EntityManagerFactory entityManagerFactory)  {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        // Skip JWT processing for login and logout only
        if (pathMatcher.match("/api/auth/login", requestPath) ||
                pathMatcher.match("/api/auth/logout", requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        String extractedEmail = null;
        String extractedRole = null;

        try {
            extractedEmail = jwtService.extractEmail(jwt);
            extractedRole = jwtService.extractRole(jwt);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Allow expired tokens for logout endpoint only
            if (pathMatcher.match("/api/auth/logout", requestPath)) {
                filterChain.doFilter(request, response);
                return;
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"JWT token has expired\"}");
                return;
            }
        }

        email = extractedEmail;
        String role = extractedRole;

        if (blacklistedTokenRepository.existsByToken(jwt)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("This token has been blacklisted.");
            return;
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null && jwtService.isTokenValid(jwt, user.getEmail())) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
