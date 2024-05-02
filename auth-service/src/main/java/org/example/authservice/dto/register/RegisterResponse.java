package org.example.authservice.dto.register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.authservice.data.enums.UserRole;
import org.springframework.http.HttpStatus;

/**
 * Author: Simeon Popov
 * Date of creation: 2.5.2024 г.
 */

@Data
@Builder
@AllArgsConstructor
public class RegisterResponse<T> {
    private String response;
    private HttpStatus httpStatus;
    private T data;
}
