package org.example.authservice.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Author: Simeon Popov
 * Date of creation: 2.5.2024 г.
 */

@Data
@AllArgsConstructor
public class UserRegistrationResponse {
    private final String response;
    private final HttpStatus httpStatus;
}
