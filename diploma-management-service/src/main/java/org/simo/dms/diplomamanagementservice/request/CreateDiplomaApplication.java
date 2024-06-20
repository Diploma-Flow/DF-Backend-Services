package org.simo.dms.diplomamanagementservice.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author: Simeon Popov
 * Date of creation: 6/19/2024
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateDiplomaApplication {
    private String title;
    private String ownerEmail;
    private String supervisorEmail;

    @Size(max = 10, message = "Tasks must not be more than 10")
    private List<@Size(max = 100) String> tasks;

    @Size(max = 20, message = "Tech stack must not be more than 20")
    private List<@Size(max = 100) String> techStack;
}
