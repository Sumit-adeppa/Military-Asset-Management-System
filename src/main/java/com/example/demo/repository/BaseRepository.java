package com.example.demo.repository;

import com.example.demo.entity.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BaseRepository extends JpaRepository<Base, Long> {
    Optional<Base> findByName(String name);
    boolean existsByName(String name);
}
