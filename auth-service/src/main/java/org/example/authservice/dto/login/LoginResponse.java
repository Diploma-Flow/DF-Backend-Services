package org.example.authservice.dto.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.authservice.data.entity.User;
import org.example.authservice.dto.Response;
import org.springframework.http.HttpStatus;

/**
 * Author: Simeon Popov
 * Date of creation: 2.5.2024 г.
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class LoginResponse extends Response {
}
