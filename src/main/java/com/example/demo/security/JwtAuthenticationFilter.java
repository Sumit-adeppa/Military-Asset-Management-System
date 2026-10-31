package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/auth/login") || path.equals("/api/auth/logout");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromCookie(request);

        if (token != null) {
            try {
                Claims claims = jwtUtil.validateToken(token);
                Long userId = Long.valueOf(claims.getSubject());
                String username = claims.get("username", String.class);
                String role = claims.get("role", String.class);
                Long baseId = claims.get("baseId", Long.class);

                AuthContext.AuthUser authUser = new AuthContext.AuthUser(userId, username, role, baseId);
                AuthContext.setUser(authUser);
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("{\"error\":\"TOKEN_EXPIRED\",\"message\":\"Session expired, please login again\"}");
                return;
            } catch (JwtException | IllegalArgumentException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("{\"error\":\"INVALID_TOKEN\",\"message\":\"Invalid or malformed token\"}");
                return;
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            AuthContext.clear();
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
