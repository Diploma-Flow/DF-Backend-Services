package org.example.userservice.exception.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Simeon Popov
 * Date of creation: 8.5.2024 г.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConstraintViolationDetail {
    private String violationConstraint;
    private String providedValue;
}
