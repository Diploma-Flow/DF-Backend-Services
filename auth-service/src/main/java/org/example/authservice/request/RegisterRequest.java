package org.example.authservice.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@Data
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
}
