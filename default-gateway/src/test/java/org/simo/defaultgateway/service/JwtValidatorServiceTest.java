package org.simo.defaultgateway.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.simo.defaultgateway.exception.AuthenticationException;
import org.simo.defaultgateway.validators.AuthValidator;
import org.simo.defaultgateway.validators.impl.AuthHeaderValidator;
import org.simo.defaultgateway.validators.impl.BearerValidator;
import org.simo.defaultgateway.validators.impl.JwtFormatValidator;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Author: Simeon Popov
 * Date of creation: 5/14/2024
 */

@ExtendWith(MockitoExtension.class)
class JwtValidatorServiceTest {

    @InjectMocks
    private JwtValidatorService jwtValidatorService;

    @Mock
    private ServerHttpRequest mockRequest;

    @Mock
    private HttpHeaders mockHeaders;

//    @Nested
//    @DisplayName("validateAuthorizationHeader method tests")
//    class ValidateAuthorizationHeaderTests {
//
//        @Test
//        @DisplayName("should throw AuthenticationException when Authorization header is missing")
//        public void testValidateAuthorizationHeader_MissingAuthorizationHeader() {
//            when(mockHeaders.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn(null);
//
//            AuthenticationException exception = assertThrows(AuthenticationException.class,
//                    () -> jwtValidatorService.validateAuthorizationHeader(mockRequest),
//                    "Expected AuthenticationException");
//
//            assertEquals("Authorization header is missing in request", exception.getMessage());
//
//            verify(mockRequest, times(1)).getHeaders();
//            verify(mockHeaders, times(1)).getFirst(HttpHeaders.AUTHORIZATION);
//        }
//
//        @Test
//        @DisplayName("should throw AuthenticationException when Authorization header is blank")
//        public void testValidateAuthorizationHeader_AuthorizationHeaderBlank() {
//            when(mockHeaders.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn("");
//
//            AuthenticationException exception = assertThrows(AuthenticationException.class,
//                    () -> jwtValidatorService.validateAuthorizationHeader(mockRequest),
//                    "Expected AuthenticationException");
//
//            assertEquals("Authorization header is empty", exception.getMessage());
//
//            verify(mockRequest, times(1)).getHeaders();
//            verify(mockHeaders, times(1)).getFirst(HttpHeaders.AUTHORIZATION);
//        }
//
//        @Test
//        @DisplayName("should throw AuthenticationException when Authorization header does not start with BEARER_PREFIX")
//        public void testValidateAuthorizationHeader_AuthorizationHeader_WithoutBearerPrefix() {
//            when(mockHeaders.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn("not bearer prefix");
//
//            AuthenticationException exception = assertThrows(AuthenticationException.class,
//                    () -> jwtValidatorService.validateAuthorizationHeader(mockRequest),
//                    "Expected AuthenticationException");
//
//            assertEquals("Bearer token is missing", exception.getMessage());
//
//            verify(mockRequest, times(1)).getHeaders();
//            verify(mockHeaders, times(1)).getFirst(HttpHeaders.AUTHORIZATION);
//        }
//
//        @Test
//        @DisplayName("should throw AuthenticationException when Authorization header's jwt token format is not valid")
//        public void testValidateAuthorizationHeader_AuthorizationHeader_InvalidJwtFormat() {
//            when(mockHeaders.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer ua");
//
//            AuthenticationException exception = assertThrows(AuthenticationException.class,
//                    () -> jwtValidatorService.validateAuthorizationHeader(mockRequest),
//                    "Expected AuthenticationException");
//
//            assertEquals("JWT format is NOT valid", exception.getMessage());
//
//            verify(mockRequest, times(1)).getHeaders();
//            verify(mockHeaders, times(1)).getFirst(HttpHeaders.AUTHORIZATION);
//        }
//    }


//    @Nested
//    @DisplayName("validateAuth method tests")
//    class ValidateAuthTests {
//
//        @Test
//        @DisplayName("should throw AuthenticationException when Authorization header is missing")
//        public void testValidateAuth_MissingAuthorizationHeader() {
//            when(mockRequest.getHeaders()).thenReturn(mockHeaders);
//            when(mockHeaders.containsKey(HttpHeaders.AUTHORIZATION)).thenReturn(false);
//
//            AuthenticationException exception = assertThrows(AuthenticationException.class,
//                    () -> jwtValidatorService.validateAuth(mockRequest),
//                    "Expected AuthenticationException");
//
//            assertEquals("Authorization header is missing in request", exception.getMessage());
//        }
//
//        @Test
//        @DisplayName("should NOT throw exception when Authorization header is found")
//        public void testValidateAuth_AuthorizationHeaderFound() {
//            when(mockRequest.getHeaders()).thenReturn(mockHeaders);
//            when(mockHeaders.containsKey(HttpHeaders.AUTHORIZATION)).thenReturn(true);
//
//            assertDoesNotThrow(
//                    () -> jwtValidatorService.validateAuth(mockRequest),
//                    "Not expected to throw exception");
//
//            verify(mockRequest, times(1)).getHeaders();
//            verify(mockHeaders, times(1)).containsKey(HttpHeaders.AUTHORIZATION);
//
//        }
//    }
//
//    @Nested
//    @DisplayName("validateAuthHeader method tests")
//    class ValidateAuthHeaderTests {
//
//        @Test
//        @DisplayName("should throw AuthenticationException when Authorization header is missing in request")
//        public void testValidateAuthHeader_MissingAuthorizationHeader() {
//            when(mockRequest.getHeaders()).thenReturn(mockHeaders);
//            when(mockHeaders.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn(null);
//
//            AuthenticationException exception = assertThrows(AuthenticationException.class,
//                    () -> jwtValidatorService.validateAuthHeader(mockRequest),
//                    "Expected AuthenticationException");
//
//            assertEquals("Authorization header is empty", exception.getMessage());
//
//            verify(mockRequest, times(1)).getHeaders();
//            verify(mockHeaders, times(1)).getFirst(HttpHeaders.AUTHORIZATION);
//        }
//
//        @Test
//        @DisplayName("should throw AuthenticationException when Authorization header is found but blank")
//        public void testValidateAuthHeader_BlankAuthorizationHeader() {
//            when(mockRequest.getHeaders()).thenReturn(mockHeaders);
//            when(mockHeaders.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn("");
//
//            AuthenticationException exception = assertThrows(AuthenticationException.class,
//                    () -> jwtValidatorService.validateAuthHeader(mockRequest),
//                    "Expected AuthenticationException");
//
//            assertEquals("Authorization header is empty", exception.getMessage());
//
//            verify(mockRequest, times(1)).getHeaders();
//            verify(mockHeaders, times(1)).getFirst(HttpHeaders.AUTHORIZATION);
//        }
//
//        @Test
//        @DisplayName("should NOT throw exception when Authorization header is found and not blank")
//        public void testValidateAuthHeader_ValidAuthorizationHeader() {
//            when(mockRequest.getHeaders()).thenReturn(mockHeaders);
//            when(mockHeaders.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn("simo");
//
//            assertDoesNotThrow(
//                    () -> jwtValidatorService.validateAuthHeader(mockRequest),
//                    "Not expected to throw exception");
//
//            verify(mockRequest, times(1)).getHeaders();
//            verify(mockHeaders, times(1)).getFirst(HttpHeaders.AUTHORIZATION);
//
//        }
//    }
//
//    @Nested
//    @DisplayName("validateBearer method tests")
//    class ValidateBearerTests {
//
//        @Test
//        @DisplayName("should throw AuthenticationException when Authorization header is missing in request")
//        public void testValidateBearer_MissingAuthorizationHeader() {
//            when(mockRequest.getHeaders()).thenReturn(mockHeaders);
//            when(mockHeaders.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn(null);
//
//            AuthenticationException exception = assertThrows(AuthenticationException.class,
//                    () -> jwtValidatorService.validateBearer(mockRequest),
//                    "Expected AuthenticationException");
//
//            assertEquals("Bearer token is missing", exception.getMessage());
//
//            verify(mockRequest, times(1)).getHeaders();
//            verify(mockHeaders, times(1)).getFirst(HttpHeaders.AUTHORIZATION);
//        }
//
//        @Test
//        @DisplayName("should throw AuthenticationException when Authorization header is found but does not start with BEARER_PREFIX")
//        public void testValidateBearer_MissingBearer_in_AuthorizationHeader() {
//            when(mockRequest.getHeaders()).thenReturn(mockHeaders);
//            when(mockHeaders.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn("no bearer");
//
//            AuthenticationException exception = assertThrows(AuthenticationException.class,
//                    () -> jwtValidatorService.validateBearer(mockRequest),
//                    "Expected AuthenticationException");
//
//            assertEquals("Bearer token is missing", exception.getMessage());
//
//            verify(mockRequest, times(1)).getHeaders();
//            verify(mockHeaders, times(1)).getFirst(HttpHeaders.AUTHORIZATION);
//        }
//
//        @Test
//        @DisplayName("should NOT throw exception when Authorization header is found and starts with BEARER_PREFIX")
//        public void testValidateBearer_ValidBearer_in_AuthorizationHeader() {
//            when(mockRequest.getHeaders()).thenReturn(mockHeaders);
//            when(mockHeaders.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn(JwtValidatorService.BEARER_PREFIX);
//
//            assertDoesNotThrow(
//                    () -> jwtValidatorService.validateBearer(mockRequest),
//                    "Not expected to throw exception");
//
//            verify(mockRequest, times(1)).getHeaders();
//            verify(mockHeaders, times(1)).getFirst(HttpHeaders.AUTHORIZATION);
//
//        }
//    }
//
//    @Nested
//    @DisplayName("validateJwtFormat method tests")
//    class ValidateJwtFormatTests {
//
//        @Test
//        @DisplayName("should throw AuthenticationException when Authorization header is missing in request")
//        public void testValidateBearer_MissingAuthorizationHeader() {
//            when(mockRequest.getHeaders()).thenReturn(mockHeaders);
//            when(mockHeaders.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn(null);
//
//            AuthenticationException exception = assertThrows(AuthenticationException.class,
//                    () -> jwtValidatorService.validateJwtFormat(mockRequest),
//                    "Expected AuthenticationException");
//
//            assertEquals("Bearer token is missing", exception.getMessage());
//
//            verify(mockRequest, times(1)).getHeaders();
//            verify(mockHeaders, times(1)).getFirst(HttpHeaders.AUTHORIZATION);
//        }
//
//
//    }

}