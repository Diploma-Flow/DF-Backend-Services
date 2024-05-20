package org.example.userservice.dto.register;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.userservice.dto.User;
import org.example.userservice.exception.helper.ConstraintViolationDetail;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Author: Simeon Popov
 * Date of creation: 2.5.2024 г.
 */

@Data
@Builder
@AllArgsConstructor
@JsonInclude(value= JsonInclude.Include.NON_EMPTY, content= JsonInclude.Include.NON_NULL)
public class RegisterUserResponse {
    private List<ConstraintViolationDetail> constraintViolations;
    private String response;
    private HttpStatus httpStatus;
}
