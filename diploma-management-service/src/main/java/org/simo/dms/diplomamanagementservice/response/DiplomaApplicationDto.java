package org.simo.dms.diplomamanagementservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simo.dms.diplomamanagementservice.enums.DiplomaStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Simeon Popov
 * Date of creation: 6/19/2024
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiplomaApplicationDto {
    private String id;
    private String title;
    private DiplomaStatus status;
    private String ownerEmail;
    private String supervisorEmail;
    private LocalDateTime creationDate;
    private List<String> tasks;
    private List<String> techStack;
}
