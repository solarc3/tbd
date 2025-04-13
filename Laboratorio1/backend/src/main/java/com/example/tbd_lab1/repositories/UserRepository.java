package com.example.tbd_lab1.repositories;

import java.util.Optional;

import com.example.tbd_lab1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);
}
