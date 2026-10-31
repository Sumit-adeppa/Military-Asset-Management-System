package com.example.demo.service.impl;

import com.example.demo.entity.Base;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BaseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BaseRepository baseRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BaseRepository baseRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.baseRepository = baseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User createUser(String username, String password, Role role, Long baseId) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username already exists: " + username);
        }
        Base base = null;
        if (baseId != null) {
            base = baseRepository.findById(baseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Base not found with id: " + baseId));
        }
        User user = new User(username, passwordEncoder.encode(password), role, base);
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAllWithBase();
    }

    @Override
    @Transactional
    public User updateUser(Long id, String username, Role role, Long baseId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        if (!user.getUsername().equals(username) && userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username already exists: " + username);
        }
        user.setUsername(username);
        user.setRole(role);
        if (baseId != null) {
            Base base = baseRepository.findById(baseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Base not found with id: " + baseId));
            user.setBase(base);
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
