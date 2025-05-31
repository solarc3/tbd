package com.example.tbd_lab2.DTO.auth;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
