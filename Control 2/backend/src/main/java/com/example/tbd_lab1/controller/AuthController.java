package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.*;
import com.example.tbd_lab1.entities.UserEntity;
import com.example.tbd_lab1.repositories.UserRepository;
import com.example.tbd_lab1.security.JwtUtils;
import com.example.tbd_lab1.security.services.RefreshTokenService;
import com.example.tbd_lab1.security.services.UserDetailsImpl;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(
            @RequestBody LoginRequest loginRequest
    ) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(
                loginRequest.getEmail()
        );

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new MessageResponse("Error: Invalid email or password!")
            );
        }

        UserEntity user = userOptional.get();

        // Authenticate with username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        ResponseCookie jwtCookie = ResponseCookie.from("SESSION", jwt)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("Lax")
                .build();

        UserDetailsImpl userDetails =
                (UserDetailsImpl) authentication.getPrincipal();
        String refreshToken = refreshTokenService.createRefreshToken(
                userDetails.getId()
        );
        ResponseCookie refreshCookie = ResponseCookie.from(
                        "REFRESH",
                        refreshToken
                )
                .httpOnly(true)
                .secure(false)
                .path("/api/auth/refreshtoken")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(
                        Map.of(
                                "id",
                                userDetails.getId(),
                                "username",
                                userDetails.getUsername(),
                                "firstName",
                                userDetails.getFirstName(),
                                "lastName",
                                userDetails.getLastName(),
                                "rut",
                                userDetails.getRut(),
                                "email",
                                userDetails.getEmail(),
                                "location",
                                userDetails.getLocation().toText()
                        )
                );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(
            @RequestBody SignupRequest signUpRequest
    ) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Check if RUT is provided
        if (
                signUpRequest.getRut() == null || signUpRequest.getRut().isEmpty()
        ) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: RUT is required!"));
        }

        // Check if RUT is already in use
        if (userRepository.existsByRut(signUpRequest.getRut())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: RUT is already in use!"));
        }

        // Validate first name and last name
        if (
                signUpRequest.getFirstName() == null ||
                        signUpRequest.getFirstName().isEmpty()
        ) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: First name is required!"));
        }

        if (
                signUpRequest.getLastName() == null ||
                        signUpRequest.getLastName().isEmpty()
        ) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Last name is required!"));
        }

        // Create new user's account
        UserEntity userEntity = UserEntity.builder()
                .username(signUpRequest.getUsername())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .rut(signUpRequest.getRut())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();

        if (signUpRequest.getLatitude() != null && signUpRequest.getLongitude() != null) {
            Point location = geometryFactory.createPoint(new Coordinate(signUpRequest.getLongitude(), signUpRequest.getLatitude()));
            userEntity.setLocation(location);
        }

        userRepository.save(userEntity);

        return ResponseEntity.ok(
                new MessageResponse("User registered successfully!")
        );
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(
            @RequestBody TokenRefreshRequest request
    ) {
        return refreshTokenService
                .findByToken(request.getRefreshToken())
                .map(userEntity -> {
                    refreshTokenService.verifyExpiration(userEntity);

                    UserDetailsImpl userDetails =
                            (UserDetailsImpl) userDetailsService.loadUserByUsername(
                                    userEntity.getUsername()
                            );

                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                    String newJwt = jwtUtils.generateJwtToken(authentication);

                    return ResponseEntity.ok(
                            new TokenRefreshResponse(
                                    newJwt,
                                    request.getRefreshToken(),
                                    "Bearer"
                            )
                    );
                })
                .orElseGet(() ->
                        (ResponseEntity<
                                TokenRefreshResponse
                                >) ResponseEntity.badRequest()
                );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);

        ResponseCookie deleteJwtCookie = ResponseCookie.from("SESSION", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        ResponseCookie deleteRefreshCookie = ResponseCookie.from("REFRESH", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refreshtoken")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteJwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, deleteRefreshCookie.toString())
                .body(new MessageResponse("Log out successful!"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (
                authentication == null ||
                        !authentication.isAuthenticated() ||
                        authentication instanceof AnonymousAuthenticationToken
        ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new MessageResponse("No authenticated")
            );
        }

        UserDetailsImpl userDetails =
                (UserDetailsImpl) authentication.getPrincipal();
        UserInfoResponse dto = UserInfoResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .rut(userDetails.getRut())
                .email(userDetails.getEmail())
                .location(userDetails.getLocation().toText())
                .build();

        return ResponseEntity.ok(dto);
    }
}