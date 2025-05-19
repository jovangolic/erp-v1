package com.jovan.erp_v1.mapper;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.response.TokenResponse;
import com.jovan.erp_v1.model.Token;

@Component
public class TokenMapper {

	public TokenResponse toResponse(Token token) {
        return TokenResponse.builder()
            .id(token.getId())
            .token(token.getToken())
            .expired(token.isExpired())
            .revoked(token.isRevoked())
            .tokenType(token.getTokenType())
            .userId(token.getUser().getId())
            .username(token.getUser().getUsername())
            .build();
    }
}
