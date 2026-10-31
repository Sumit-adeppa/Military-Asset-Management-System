package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import java.util.List;

public interface UserService {
    User createUser(String username, String password, Role role, Long baseId);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, String username, Role role, Long baseId);
    void deleteUser(Long id);
}
