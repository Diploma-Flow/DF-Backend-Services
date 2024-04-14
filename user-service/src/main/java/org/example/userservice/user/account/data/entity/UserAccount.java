package org.example.userservice.user.account.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.userservice.user.details.data.entity.UserDetails;
import org.example.userservice.user.details.data.enums.UserRole;
import org.example.userservice.user.shared.BaseEntity;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_account")
public class UserAccount extends BaseEntity {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 5, message = "Username must be at least 5 characters long")
    @Pattern(regexp = "@\\w*\\d*")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @Enumerated
    @NotNull(message = "Role cannot be null")
    private UserRole role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_details_id")
    private UserDetails userDetails;
}
