package org.example.authservice.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.example.authservice.data.TokenType;
import org.example.authservice.data.entity.User;
import org.example.authservice.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@Data
@Service
public class JwtServiceImpl implements JwtService {

    public static final int BEARER_LENGTH = 7;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public static String extractTokenFromHeader(String header) {
        return header.substring(BEARER_LENGTH);
    }

    @Override
    public String generateToken(User user, TokenType tokenType) {
        long expiration = getExpirationByType(tokenType);

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiredAt = new Date(System.currentTimeMillis() + expiration);

        Map<String, Object> extraClaims = Map.of("role", user.getRole());

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Map<TokenType, String> generateJwtTokens(User user) {
        String accessToken = generateToken(user, TokenType.ACCESS);
        String refreshToken = generateToken(user, TokenType.REFRESH);

        return Map.of(TokenType.ACCESS, accessToken, TokenType.REFRESH, refreshToken);
    }

    private long getExpirationByType(TokenType tokenType) {
        return (tokenType == TokenType.ACCESS) ? accessTokenExpiration : refreshTokenExpiration;
    }

    private Key getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractEmail(String jsonWebToken) {
        return extractClaim(jsonWebToken, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String jsonWebToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jsonWebToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jsonWebToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(jsonWebToken)
                .getBody();
    }

    private Date extractExpiration(String jsonWebToken) {
        return extractClaim(jsonWebToken, Claims::getExpiration);
    }

    @Override
    public boolean notExpired(String jsonWebToken){
        return extractExpiration(jsonWebToken).before(new Date());
    }
}
