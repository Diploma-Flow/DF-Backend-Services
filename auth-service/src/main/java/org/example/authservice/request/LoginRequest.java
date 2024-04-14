package org.example.authservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@Data
@AllArgsConstructor
public class LoginRequest {
    private final String email;
    private final String password;
}
