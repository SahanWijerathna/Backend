package com.example.resqdrive.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        String email = null;

        // 1. Try to read from Authorization Header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // 2. Fallback: Try to read from Cookie (satisfies Assignment cookie flow requirements)
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("vamp-token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            try {
                email = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);
                String id = jwtUtil.extractId(token);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtil.validateToken(token, email)) {
                        // Store the user id, email, and role inside a CustomPrincipal or as UsernamePasswordAuthenticationToken Principal
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                new CustomPrincipal(id, email, role),
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                // Token parser issues, invalid token
                logger.error("Error verifying JWT token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    // Helper class to store custom properties inside Spring security principal
    public static class CustomPrincipal {
        private final String id;
        private final String email;
        private final String role;

        public CustomPrincipal(String id, String email, String role) {
            this.id = id;
            this.email = email;
            this.role = role;
        }

        public String getId() { return id; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
    }
}
