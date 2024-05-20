package org.example.authservice.response;

import lombok.*;
import org.example.authservice.data.entity.User;
import org.springframework.http.HttpStatus;

/**
 * Author: Simeon Popov
 * Date of creation: 2.5.2024 г.
 */

@Data
@AllArgsConstructor
public class LoginResponse {
    private final String response;
    private final HttpStatus httpStatus;
    private final User user;
}
