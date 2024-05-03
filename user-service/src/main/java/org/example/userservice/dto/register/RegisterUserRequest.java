package org.example.userservice.dto.register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.userservice.enums.UserRole;

/**
 * Author: Simeon Popov
 * Date of creation: 3.5.2024 г.
 */

@Data
@AllArgsConstructor
@Builder
public class RegisterUserRequest {
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final UserRole userRole;
}
