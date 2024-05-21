package org.example.authservice.client.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.authservice.enums.UserRole;

/**
 * Author: Simeon Popov
 * Date of creation: 3.5.2024 г.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole userRole;
}
