package com.example.tbd_lab1.entities;
import jakarta.persistence.Table;
import org.locationtech.jts.geom.Point;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class UserEntity {
    @Id
    private Long id;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    private Point location;

    private String username;
    private String firstName;
    private String lastName;
    private String rut;
    private String password;
    private String email;

    private String refreshToken;
    private Long refreshTokenExpiration;
}