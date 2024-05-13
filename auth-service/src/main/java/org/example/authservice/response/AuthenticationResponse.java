package org.example.authservice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value= JsonInclude.Include.NON_EMPTY, content= JsonInclude.Include.NON_NULL)
public class AuthenticationResponse<T> {
    private String response;
    private HttpStatus httpStatus;
    private T data;
    private ZonedDateTime timestamp;
}
