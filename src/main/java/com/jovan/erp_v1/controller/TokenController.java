package com.jovan.erp_v1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.RefreshTokenRequest;
import com.jovan.erp_v1.response.TokenResponse;
import com.jovan.erp_v1.service.ITokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class TokenController {

	private final ITokenService tokenService;

    @PostMapping("/revoke/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> revokeToken(@PathVariable Long id) {
        tokenService.revokeToken(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse tokenResponse = tokenService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(tokenResponse);
    }
    
}
