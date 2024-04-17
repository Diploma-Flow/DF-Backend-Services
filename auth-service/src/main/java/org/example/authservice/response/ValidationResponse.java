package org.example.authservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Simeon Popov
 * Date of creation: 17.4.2024 г.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponse {
    @JsonProperty("valid_token")
    private Boolean validToken;
}
