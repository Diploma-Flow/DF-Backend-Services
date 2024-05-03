package org.example.userservice.dto.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@Data
@Builder
@AllArgsConstructor
public class LoginRequest {
    private final String email;
    private final String password;
}
