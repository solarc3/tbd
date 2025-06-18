package com.example.tbd_lab2.security.services;

import java.util.Optional;

import com.example.tbd_lab2.entities.UserEntity;
import com.example.tbd_lab2.repositories.UserRepository;
import com.example.tbd_lab2.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public Optional<UserEntity> findByToken(String token) {
        return userRepository.findByRefreshToken(token);
    }

    public String createRefreshToken(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        String refreshToken = jwtUtils.generateRefreshToken();
        long refreshTokenExpiryTime = jwtUtils.getRefreshTokenExpiryTime();

        userRepository.updateRefreshToken(userId, refreshToken, refreshTokenExpiryTime);

        return refreshToken;
    }

    public String verifyExpiration(UserEntity userEntity) {
        if (userEntity.getRefreshTokenExpiration() == null || userEntity.getRefreshTokenExpiration() < System.currentTimeMillis()) {
            userRepository.clearRefreshToken(userEntity.getId());
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return userEntity.getRefreshToken();
    }

    public void deleteByUserId(Long userId) {
        // no usa transactional pq es una sola consulta
        userRepository.clearRefreshToken(userId);
    }
}