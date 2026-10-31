package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    List<User> findByRole(Role role);
    List<User> findByBaseId(Long baseId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.base")
    List<User> findAllWithBase();
}
