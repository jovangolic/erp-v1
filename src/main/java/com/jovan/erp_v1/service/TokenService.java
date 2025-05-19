package com.jovan.erp_v1.service;


import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.TokenType;
import com.jovan.erp_v1.exception.InvalidTokenException;
import com.jovan.erp_v1.exception.TokenNotFoundException;
import com.jovan.erp_v1.mapper.TokenMapper;
import com.jovan.erp_v1.model.Role;
import com.jovan.erp_v1.model.Token;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.TokenRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.response.TokenResponse;
import com.jovan.erp_v1.util.ApiMessages;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.security.core.userdetails.User.withUsername;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService implements ITokenService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;

    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.access.token.validity}")
    private long accessTokenValidity;
    
    @Value("${jwt.refresh.token.validity}")
    private long refreshTokenValidity;
    
    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    @Transactional
    public void saveTokenForUser(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .tokenType(TokenType.ACCESS)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public void revokeAllTokensForUser(User user) {
        List<Token> validTokens = tokenRepository.findAllByUser(user)
                .stream()
                .filter(t -> !t.isExpired() && !t.isRevoked())
                .toList();

        validTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });

        tokenRepository.saveAll(validTokens);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void validateToken(String token) throws InvalidTokenException {
    	log.info("Validating token: {}", token);
    	Token tokenEntity = tokenRepository.findByToken(token)
    	        .orElseThrow(() -> new InvalidTokenException("Token not found."));
    	log.info("Token found: {} | Revoked: {} | Expired: {}", tokenEntity.getToken(), tokenEntity.isRevoked(), tokenEntity.isExpired());
    	if (tokenEntity.isExpired() || tokenEntity.isRevoked()) {
    		    throw new InvalidTokenException("Token is revoked or expired.");
    	}
    }

    @Override
    @Transactional
    public void invalidateToken(String token) {
        tokenRepository.findByToken(token).ifPresent(tokenRepository::delete);
    }

    @Override
    public String getUsernameFromToken(String token) throws InvalidTokenException {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    
    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
            throws InvalidTokenException {
        val claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims getAllClaimsFromToken(String token) throws InvalidTokenException {
        try {
        	log.info("Validating token: {}", token);
        	Token tokenEntity = tokenRepository.findByToken(token)
        	        .orElseThrow(() -> new InvalidTokenException("Token not found."));
        	log.info("Token found: {} | Revoked: {} | Expired: {}", tokenEntity.getToken(), tokenEntity.isRevoked(), tokenEntity.isExpired());
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

        } catch (ExpiredJwtException e) {
            // Delete expired token
            invalidateToken(token);

            throw new InvalidTokenException(ApiMessages.TOKEN_EXPIRED_ERROR.getMessage());

        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException(ApiMessages.TOKEN_UNSUPPORTED_ERROR.getMessage());

        } catch (MalformedJwtException e) {
            throw new InvalidTokenException(ApiMessages.TOKEN_MALFORMED_ERROR.getMessage());

        } catch (SignatureException e) {
            throw new InvalidTokenException(ApiMessages.TOKEN_SIGNATURE_INVALID_ERROR.getMessage());

        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException(ApiMessages.TOKEN_EMPTY_ERROR.getMessage());
        }
    }

    @Override
    @Transactional
    public TokenResponse refreshToken(String refreshToken) {
        // Validacija da li postoji
        Token oldToken = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidTokenException("Token not found."));
        // Provera da li je refresh token i da nije istekao/revokovan
        if (oldToken.getTokenType() != TokenType.REFRESH || oldToken.isExpired() || oldToken.isRevoked()) {
            throw new InvalidTokenException("Invalid refresh token.");
        }
        // Parsiranje subject-a iz tokena
        String username = getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        // Kreiranje novih tokena
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList())
                .build();
        // Revoke stari refresh token
        oldToken.setRevoked(true);
        oldToken.setExpired(true);
        tokenRepository.save(oldToken);
        // Novi access token
        String accessToken = generateAccessToken(user);
        Token newAccessToken = Token.builder()
                .user(user)
                .token(accessToken)
                .expired(false)
                .revoked(false)
                .tokenType(TokenType.ACCESS)
                .build();
        tokenRepository.save(newAccessToken);

        // Novi refresh token
        String newRefreshToken = generateRefreshToken(userDetails);
        Token newRefreshTokenEntity = Token.builder()
                .user(user)
                .token(newRefreshToken)
                .expired(false)
                .revoked(false)
                .tokenType(TokenType.REFRESH)
                .build();
        tokenRepository.save(newRefreshTokenEntity);
        return TokenResponse.builder()
                .id(newAccessToken.getId())
                .token(newAccessToken.getToken())
                .expired(newAccessToken.isExpired())
                .revoked(newAccessToken.isRevoked())
                .tokenType(newAccessToken.getTokenType())
                .refreshToken(newRefreshTokenEntity.getToken())
                .userId(user.getId())
                .username(user.getUsername())
                .build();// Dodaj ovo polje u TokenResponse ako treba
    }

	@Override
	@Transactional
	public void revokeToken(Long tokenId) {
	    Token token = tokenRepository.findById(tokenId)
	            .orElseThrow(() -> new TokenNotFoundException("Token not found with ID: " + tokenId));
	    // Oznaka tokena kao "revoked" i "expired"
	    token.setRevoked(true);
	    token.setExpired(true);
	    tokenRepository.save(token);
	}
	
	@Override
	public String generateAccessToken(User user) {
	    Map<String, Object> claims = new HashMap<>();

	    // Dodajemo uloge u payload tokena
	    claims.put("roles", user.getRoles()
	        .stream()
	        .map(Role::getName)
	        .collect(Collectors.toList()));

	    return Jwts.builder()
	        .setClaims(claims)
	        .setSubject(user.getUsername()) // ili user.getEmail() ako koristiš email kao subject
	        .setIssuedAt(new Date())
	        .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity)) // npr. 10*60*1000
	        .signWith(SignatureAlgorithm.HS512, secret)
	        .compact();
	}
	
	
	public String generateRefreshToken(UserDetails userDetails) {
	    return Jwts.builder()
	            .setSubject(userDetails.getUsername())
	            .setIssuedAt(new Date())
	            .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
	            .signWith(SignatureAlgorithm.HS512, secret)
	            .compact();
	}

}