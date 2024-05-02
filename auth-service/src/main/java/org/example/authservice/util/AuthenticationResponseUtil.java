package org.example.authservice.util;

import org.example.authservice.data.TokenType;
import org.example.authservice.response.AuthenticationResponse;
import org.example.authservice.response.TokenData;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Author: Simeon Popov
 * Date of creation: 3.5.2024 г.
 */
public class AuthenticationResponseUtil {
    public static AuthenticationResponse<TokenData> buildAuthResponseError(HttpStatus httpStatus, String message) {
        return AuthenticationResponse
                .<TokenData>builder()
                .httpStatus(httpStatus)
                .response(message)
                .build();
    }

    public static AuthenticationResponse<TokenData> buildAuthResponseOk(Map<TokenType, String> jwtTokens, String message) {
        return AuthenticationResponse
                .<TokenData>builder()
                .httpStatus(HttpStatus.OK)
                .data(TokenData
                        .builder()
                        .accessToken(jwtTokens.get(TokenType.ACCESS))
                        .refreshToken(jwtTokens.get(TokenType.REFRESH))
                        .build())
                .response(message)
                .build();
    }
}
