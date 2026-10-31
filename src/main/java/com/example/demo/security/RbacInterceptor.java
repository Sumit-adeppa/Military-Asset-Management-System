package com.example.demo.security;

import com.example.demo.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class RbacInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!AuthContext.isAuthenticated()) {
            sendError(response, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Authentication required");
            return false;
        }

        String uri = request.getRequestURI();
        String roleStr = AuthContext.getRole();

        if (roleStr == null) {
            sendError(response, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Invalid token: role claim missing");
            return false;
        }

        Role role;
        try {
            role = Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            sendError(response, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Invalid role in token");
            return false;
        }

        Long userBaseId = AuthContext.getBaseId();

        if (role == Role.ADMIN) {
            return true;
        }

        if (uri.startsWith("/api/users") || uri.startsWith("/api/bases") || uri.startsWith("/api/equipment-types")
                || uri.startsWith("/api/audit-logs")) {
            sendError(response, HttpStatus.FORBIDDEN, "FORBIDDEN", "Admin access required");
            return false;
        }

        if (role == Role.BASE_COMMANDER) {
            Long requestedBaseId = extractBaseIdFromRequest(request);
            if (requestedBaseId != null && !requestedBaseId.equals(userBaseId)) {
                sendError(response, HttpStatus.FORBIDDEN, "FORBIDDEN",
                        "Access denied: cannot access data for another base");
                return false;
            }
        }

        if (role == Role.LOGISTICS_OFFICER) {
            if (uri.startsWith("/api/assignments") || uri.startsWith("/api/expenditures")) {
                sendError(response, HttpStatus.FORBIDDEN, "FORBIDDEN",
                        "Logistics Officer cannot access this resource");
                return false;
            }
        }

        return true;
    }

    private Long extractBaseIdFromRequest(HttpServletRequest request) {
        String baseIdParam = request.getParameter("baseId");
        if (baseIdParam != null) {
            try {
                return Long.valueOf(baseIdParam);
            } catch (NumberFormatException ignored) {
            }
        }

        String uri = request.getRequestURI();
        String[] parts = uri.split("/");
        for (int i = 0; i < parts.length - 1; i++) {
            if ("bases".equals(parts[i])) {
                try {
                    return Long.valueOf(parts[i + 1]);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return null;
    }

    private void sendError(HttpServletResponse response, HttpStatus status, String error, String message)
            throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        String json = String.format(
                "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"message\":\"%s\"}",
                java.time.LocalDateTime.now().toString(),
                status.value(),
                error,
                message.replace("\"", "\\\"")
        );
        response.getWriter().write(json);
    }
}
