package com.example.tbd_lab1.security.services;

import java.util.Collection;
import java.util.Collections;

import com.example.tbd_lab1.entities.UserEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Getter
    private Long id;

    private String username;

    @Getter
    private String email;

    @Getter
    private String firstName;

    @Getter
    private String lastName;

    @JsonIgnore
    private String password;

    @Getter
    private String rut;

    @Getter
    private Point location;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(UserEntity userEntity) {

        return UserDetailsImpl.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .rut(userEntity.getRut())
                .password(userEntity.getPassword())
                .location(userEntity.getLocation())
                .authorities(Collections.emptyList())
                .build();
    }

    @Override


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}