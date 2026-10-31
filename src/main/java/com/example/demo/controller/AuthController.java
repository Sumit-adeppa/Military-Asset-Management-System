package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        var user = authService.validateCredentials(request.getUsername(), request.getPassword());

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                user.getBase() != null ? user.getBase().getId() : null);

        Cookie cookie = new Cookie("auth_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtExpiration / 1000));
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);

        LoginResponse loginResponse = new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getBase() != null ? user.getBase().getId() : null,
                user.getBase() != null ? user.getBase().getName() : null);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("auth_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
