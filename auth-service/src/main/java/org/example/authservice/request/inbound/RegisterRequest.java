package org.example.authservice.request.inbound;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@Data
@AllArgsConstructor
public class RegisterRequest {
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
}
