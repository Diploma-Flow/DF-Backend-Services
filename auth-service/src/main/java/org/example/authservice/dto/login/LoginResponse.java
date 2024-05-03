package org.example.authservice.dto.login;

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
public class LoginResponse {
    private String response;
    private HttpStatus httpStatus;
    private Object data;
}
