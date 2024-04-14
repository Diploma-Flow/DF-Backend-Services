package org.example.userservice.user.details.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.userservice.user.account.data.entity.UserAccount;
import org.example.userservice.user.details.data.enums.UserGender;
import org.example.userservice.user.shared.BaseEntity;

import java.time.LocalDateTime;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_details")
public class UserDetails extends BaseEntity {

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 255, message = "First name cannot exceed 255 characters")
    private String firstName;

    @Size(max = 255, message = "Middle name cannot exceed 255 characters")
    private String middleName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 255, message = "Last name cannot exceed 255 characters")
    private String lastName;

    @Enumerated
    private UserGender gender;

    @Past(message = "Date of birth must be in the past")
    private LocalDateTime dateOfBirth;

    @Size(max = 300, message = "Bio cannot exceed 300 characters")
    private String bio;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "userDetails")
    @JsonIgnore
    private UserAccount userAccount;
}
