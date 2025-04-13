package com.example.tbd_lab1.security.services;

import java.util.Optional;

import com.example.tbd_lab1.entities.User;
import com.example.tbd_lab1.repositories.UserRepository;
import com.example.tbd_lab1.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public Optional<User> findByToken(String token) {
        return userRepository.findByRefreshToken(token);
    }

    public String createRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        String refreshToken = jwtUtils.generateRefreshToken();
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiration(jwtUtils.getRefreshTokenExpiryTime());
        userRepository.save(user);

        return refreshToken;
    }

    public String verifyExpiration(User user) {
        if (user.getRefreshTokenExpiration() < System.currentTimeMillis()) {
            user.setRefreshToken(null);
            user.setRefreshTokenExpiration(null);
            userRepository.save(user);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }

        return user.getRefreshToken();
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setRefreshToken(null);
        user.setRefreshTokenExpiration(null);
        userRepository.save(user);
    }
}