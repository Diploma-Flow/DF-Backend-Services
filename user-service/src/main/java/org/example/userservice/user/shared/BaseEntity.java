package org.example.userservice.user.shared;

import jakarta.persistence.*;
import lombok.*;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */

@Data
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
