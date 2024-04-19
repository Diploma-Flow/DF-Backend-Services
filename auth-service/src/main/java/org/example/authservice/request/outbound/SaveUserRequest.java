package org.example.authservice.request.outbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Author: Simeon Popov
 * Date of creation: 19.4.2024 г.
 */

@Data
@Builder
@AllArgsConstructor
public class SaveUserRequest {
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
}
