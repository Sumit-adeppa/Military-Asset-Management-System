package com.example.demo.security;

public class AuthContext {

    private static final ThreadLocal<AuthUser> currentUser = new ThreadLocal<>();

    public static void setUser(AuthUser user) {
        currentUser.set(user);
    }

    public static AuthUser getUser() {
        return currentUser.get();
    }

    public static Long getUserId() {
        AuthUser user = currentUser.get();
        return user != null ? user.getUserId() : null;
    }

    public static String getUsername() {
        AuthUser user = currentUser.get();
        return user != null ? user.getUsername() : null;
    }

    public static String getRole() {
        AuthUser user = currentUser.get();
        return user != null ? user.getRole() : null;
    }

    public static Long getBaseId() {
        AuthUser user = currentUser.get();
        return user != null ? user.getBaseId() : null;
    }

    public static void clear() {
        currentUser.remove();
    }

    public static boolean isAuthenticated() {
        return currentUser.get() != null;
    }

    public static class AuthUser {
        private Long userId;
        private String username;
        private String role;
        private Long baseId;

        public AuthUser() {}

        public AuthUser(Long userId, String username, String role, Long baseId) {
            this.userId = userId;
            this.username = username;
            this.role = role;
            this.baseId = baseId;
        }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Long getBaseId() { return baseId; }
        public void setBaseId(Long baseId) { this.baseId = baseId; }
    }
}
