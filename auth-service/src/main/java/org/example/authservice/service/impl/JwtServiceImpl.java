package org.example.authservice.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.example.authservice.enums.TokenType;
import org.example.authservice.dto.User;
import org.example.authservice.enums.UserRole;
import org.example.authservice.exception.exceptions.HeaderValidationException;
import org.example.authservice.exception.exceptions.InvalidJwtTokenException;
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

    public static final String BEARER_PREFIX = "Bearer ";
    private static final String JWT_VALIDATION_REGEX = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_.+/=]*$";

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    @Override
    public String getJwtFromHeader(String authHeader) {
        validateAuthHeaderNotBlank(authHeader);

        if (!authHeader.startsWith(BEARER_PREFIX)) {
            throw new HeaderValidationException("Bearer token is missing");
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        if (!token.matches(JWT_VALIDATION_REGEX)) {
            throw new HeaderValidationException("JWT format is NOT valid");
        }

        return token;
    }

    private static void validateAuthHeaderNotBlank(String authHeader) {
        if (StringUtils.isBlank(authHeader)) {
            throw new HeaderValidationException("Authorization header is empty");
        }
    }

    @Override
    public String generateToken(User user, TokenType tokenType) {
        long expiration = getExpirationByType(tokenType);

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiredAt = new Date(System.currentTimeMillis() + expiration);

        Map<String, Object> extraClaims = Map.of(
                "role", user.getRole(),
                "type", tokenType.name());

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
    public TokenType extractTokenType(String jsonWebToken) {
        String tokenType = extractClaim(jsonWebToken, claims -> claims.get("type", String.class));
        return TokenType.valueOf(tokenType.toUpperCase());
    }

    @Override
    public boolean isTokenType(String jsonWebToken, TokenType tokenType) {
        TokenType extractTokenType = extractTokenType(jsonWebToken);
        return extractTokenType.equals(tokenType);
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
    public boolean checkNotExpired(String jsonWebToken){
        return extractExpiration(jsonWebToken).before(new Date());
    }

    @Override
    public UserRole extractUserRole(String jsonWebToken) {
        String role = extractClaim(jsonWebToken, claims -> claims.get("role", String.class));
        return UserRole.valueOf(role.toUpperCase());
    }

    @Override
    public void verifyAccessTokenType(String jwtToken) {
        boolean isAccessToken = isTokenType(jwtToken, TokenType.ACCESS);
        if (!isAccessToken) {
            throw new InvalidJwtTokenException("Wrong token provided");
        }
    }

    @Override
    public void verifyRefreshTokenType(String jwtToken) {
        boolean isRefreshToken = isTokenType(jwtToken, TokenType.REFRESH);
        if (!isRefreshToken) {
            throw new InvalidJwtTokenException("Wrong token provided");
        }
    }
}
