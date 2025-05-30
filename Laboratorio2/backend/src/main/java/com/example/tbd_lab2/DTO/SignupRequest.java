package com.example.tbd_lab2.DTO;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String rut;
    private String email;
    private String password;
    private Double latitude;
    private Double longitude;
}
