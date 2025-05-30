package com.example.tbd_lab2.entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.locationtech.jts.geom.Point;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    private Point location;

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