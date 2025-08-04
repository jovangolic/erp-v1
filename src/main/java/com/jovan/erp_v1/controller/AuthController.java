package com.jovan.erp_v1.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.model.Role;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.LoginRequest;
import com.jovan.erp_v1.request.RefreshTokenRequest;
import com.jovan.erp_v1.request.UserRequest;
import com.jovan.erp_v1.response.AuthResponse;
import com.jovan.erp_v1.response.TokenResponse;
import com.jovan.erp_v1.response.UserResponse;
import com.jovan.erp_v1.security.JwtService;
import com.jovan.erp_v1.service.ITokenService;
import com.jovan.erp_v1.service.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;
    private final ITokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest request) {
        try {
            UserResponse registeredUser = userService.registerUser(request);
            return ResponseEntity.ok(registeredUser); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.username())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }
        if (userRepository.existsByEmail(userRequest.email())) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }
        userService.createUserByAdmin(userRequest);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-user")
    public ResponseEntity<?> createUserByAdmin(@RequestBody UserRequest userRequest) {
        try {
            UserResponse createdUser = userService.createUserByAdmin(userRequest);
            return ResponseEntity.ok(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.identifier(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("Username: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());
        User user = userService.getUserByIdentifier(request.identifier());
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        tokenService.saveTokenForUser(user, jwtToken);
        return ResponseEntity.ok(
                new AuthResponse(
                        user.getId(),
                        user.getEmail(),
                        jwtToken,
                        refreshToken,
                        user.getRoles().stream().map(Role::getName).toList()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse tokenResponse = tokenService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/reset-password-test")
    public ResponseEntity<?> resetPasswordTest(@RequestParam String email, @RequestParam String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok("Password updated");
    }

    @GetMapping("/test-password")
    public void testPassword() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches("milan10", "$2a$10$zhPfNbyXtUjGfMDGyxknweFamuGCdDguXKaF7sqyjooby2pynQAPK");
        System.out.println("Password match? " + matches);
    }
}
