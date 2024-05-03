package org.example.userservice.dto.register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Author: Simeon Popov
 * Date of creation: 2.5.2024 г.
 */

@Data
@Builder
@AllArgsConstructor
public class RegisterUserResponse<T> {
    private String response;
    private HttpStatus httpStatus;
    private T user;
}
