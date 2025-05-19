package com.example.tbd_lab1.DTO;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String rut;
    private String email;
    private String password;
}
