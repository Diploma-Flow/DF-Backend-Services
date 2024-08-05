package org.example.userservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(value= JsonInclude.Include.NON_EMPTY, content= JsonInclude.Include.NON_NULL)
public class UserDto {
    private String username;
    private String email;
    private UserRole role;
    private String firstName;
    private String middleName;
    private String lastName;
}
