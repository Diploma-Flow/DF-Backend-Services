package org.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.userservice.enums.UserRole;

/**
 * Author: Simeon Popov
 * Date of creation: 6/20/2024
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private UserRole role;
    private String firstName;
    private String middleName;
    private String lastName;
}
