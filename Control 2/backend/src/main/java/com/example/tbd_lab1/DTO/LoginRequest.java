package com.example.tbd_lab1.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
