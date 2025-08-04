package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.TokenType;
import com.jovan.erp_v1.model.Token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {

	private Long id;
	private String token;
    private boolean expired;
    private boolean revoked;
    private TokenType tokenType;
    private String refreshToken;
    private Long userId;
    private String username;
    
    public TokenResponse(Token token) {
        this.id = token.getId();
        this.token = token.getToken();
        this.expired = token.isExpired();
        this.revoked = token.isRevoked();
        this.tokenType = token.getTokenType();
        this.userId = token.getUser().getId();
        this.username = token.getUser().getUsername();
    }
}
