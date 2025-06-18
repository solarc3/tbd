package com.example.tbd_lab2.DTO.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
