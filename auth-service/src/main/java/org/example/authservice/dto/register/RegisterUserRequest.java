package org.example.authservice.dto.register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.authservice.data.enums.UserRole;

/**
 * Author: Simeon Popov
 * Date of creation: 3.5.2024 г.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole userRole;
}
