package com.example.demo.dto.response;

import com.example.demo.enums.Role;

public class LoginResponse {

    private Long userId;
    private String username;
    private Role role;
    private Long baseId;
    private String baseName;
    private String token;

    public LoginResponse() {}

    public LoginResponse(Long userId, String username, Role role, Long baseId, String baseName) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.baseId = baseId;
        this.baseName = baseName;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Long getBaseId() { return baseId; }
    public void setBaseId(Long baseId) { this.baseId = baseId; }
    public String getBaseName() { return baseName; }
    public void setBaseName(String baseName) { this.baseName = baseName; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
