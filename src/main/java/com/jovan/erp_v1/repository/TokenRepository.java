package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Token;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.enumeration.TokenType;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	Optional<Token> findByToken(String token);
    void deleteByToken(String token);
    List<Token> findAllByUser(User user);
    
    @Query("SELECT t FROM Token t WHERE t.user.id = :userId")
    List<Token> findAllTokenByUser(@Param("userId") Long userId);
    Optional<Token> findByTokenAndTokenType(String token, TokenType tokenType);
}
