package org.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.userservice.enums.UserRole;


/**
 * Author: Simeon Popov
 * Date of creation: 19.4.2024 г.
 */

@Data
@Builder
@AllArgsConstructor
public class User {
    private final String email;
    private final UserRole role;
    private final String password;
}
