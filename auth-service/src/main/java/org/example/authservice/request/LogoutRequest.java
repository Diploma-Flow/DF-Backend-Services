package org.example.authservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Simeon Popov
 * Date of creation: 5/21/2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequest {
    private String accessToken;
}
