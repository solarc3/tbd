package com.example.tbd_lab2.entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String rut;
    private String password;
    private String email;
    private String refreshToken;
    private Long refreshTokenExpiration;
}