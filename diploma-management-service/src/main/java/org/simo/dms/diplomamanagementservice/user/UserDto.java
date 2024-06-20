package org.simo.dms.diplomamanagementservice.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simo.dms.diplomamanagementservice.enums.UserRole;

/**
 * Author: Simeon Popov
 * Date of creation: 6/19/2024
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private UserRole role;
}
