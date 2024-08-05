package org.simo.dms.diplomamanagementservice.exception.helper;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@Data
@Builder
@RequiredArgsConstructor
public class ApiException {
    private final String response;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}
