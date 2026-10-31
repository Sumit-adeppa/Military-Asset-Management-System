package com.example.demo.dto.response;

import com.example.demo.enums.Role;

public class UserResponse {

    private Long id;
    private String username;
    private Role role;
    private Long baseId;
    private String baseName;

    public UserResponse() {}

    public UserResponse(Long id, String username, Role role, Long baseId, String baseName) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.baseId = baseId;
        this.baseName = baseName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Long getBaseId() { return baseId; }
    public void setBaseId(Long baseId) { this.baseId = baseId; }
    public String getBaseName() { return baseName; }
    public void setBaseName(String baseName) { this.baseName = baseName; }
}
