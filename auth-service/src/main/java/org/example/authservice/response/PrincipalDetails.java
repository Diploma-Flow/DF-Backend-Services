package org.example.authservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.authservice.enums.UserRole;
import org.springframework.http.HttpStatus;

/**
 * Author: Simeon Popov
 * Date of creation: 18.4.2024 г.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrincipalDetails {
    private String userEmail;
    private UserRole userRole;
}
