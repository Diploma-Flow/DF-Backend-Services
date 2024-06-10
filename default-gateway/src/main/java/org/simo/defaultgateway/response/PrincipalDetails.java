package org.simo.defaultgateway.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simo.defaultgateway.enums.UserRole;

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
