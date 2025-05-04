package com.example.tbd_lab1.controller;

import com.example.tbd_lab1.DTO.*;
import com.example.tbd_lab1.entities.UserEntity;
import com.example.tbd_lab1.repositories.UserRepository;
import com.example.tbd_lab1.security.JwtUtils;
import com.example.tbd_lab1.security.services.RefreshTokenService;
import com.example.tbd_lab1.security.services.UserDetailsImpl;
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

import java.time.Duration;
import java.util.Map;


@CrossOrigin(
    origins = "http://localhost:3000",
    allowCredentials = "true",
    maxAge = 3600
)
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

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        ResponseCookie jwtCookie = ResponseCookie.from("SESSION", jwt)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Duration.ofHours(1))
            .sameSite("Lax")
            .build();

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH", refreshToken)
            .httpOnly(true)
            .secure(false)
            .path("/api/auth/refreshtoken")
            .maxAge(Duration.ofDays(7))
            .sameSite("Lax")
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .body(Map.of(
                "id",       userDetails.getId(),
                "username", userDetails.getUsername(),
                "email",    userDetails.getEmail()
                        ));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        UserEntity userEntity = UserEntity.builder()
            .username(signUpRequest.getUsername())
            .email(signUpRequest.getEmail())
            .password(encoder.encode(signUpRequest.getPassword()))
            .build();

        userRepository.save(userEntity);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
            .map(userEntity -> {
                refreshTokenService.verifyExpiration(userEntity);

                UserDetailsImpl userDetails =
                    (UserDetailsImpl) userDetailsService.loadUserByUsername(userEntity.getUsername());

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

                String newJwt = jwtUtils.generateJwtToken(authentication);

                return ResponseEntity.ok(
                    new TokenRefreshResponse(newJwt, request.getRefreshToken(), "Bearer")
                                        );
            })
            .orElseGet(() ->
                           (ResponseEntity<TokenRefreshResponse>) ResponseEntity.badRequest()
                      );
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        UserDetailsImpl userDetails =
            (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();

        if (authentication == null
            || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponse("No authenticated"));
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserInfoResponse dto = UserInfoResponse.builder()
            .id(userDetails.getId())
            .username(userDetails.getUsername())
            .email(userDetails.getEmail())
            .build();

        return ResponseEntity.ok(dto);
    }
}