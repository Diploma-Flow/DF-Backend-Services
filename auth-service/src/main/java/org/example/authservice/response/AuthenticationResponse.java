package org.example.authservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse<T> {
    private String response;
    private HttpStatus httpStatus;
    private T data;
}
