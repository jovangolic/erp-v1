package com.jovan.erp_v1.service;

import java.util.Optional;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import com.jovan.erp_v1.exception.InvalidTokenException;
import com.jovan.erp_v1.model.Token;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.response.TokenResponse;

import io.jsonwebtoken.Claims;


public interface ITokenService {

	public String generateToken(UserDetails userDetails);
	void saveTokenForUser(User user, String jwtToken);

    void revokeAllTokensForUser(User user);

    Optional<Token> findByToken(String token);
    public void validateToken(String token) throws InvalidTokenException;

    public void invalidateToken(String token);
    public String getUsernameFromToken(String token) throws InvalidTokenException;
    
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
            throws InvalidTokenException;
    
	public void revokeToken(Long id);
	
	public TokenResponse refreshToken(String refreshToken);
	
	public String generateAccessToken(User user);
}
