package org.simo.dms.diplomamanagementservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.simo.dms.diplomamanagementservice.enums.DiplomaStatus;
import org.simo.dms.diplomamanagementservice.generator.PrefixSequenceIdGenerator;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Simeon Popov
 * Date of creation: 6/19/2024
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class DiplomaApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diploma_application_seq")
    @GenericGenerator(
            name = "diploma_application_seq",
            type = PrefixSequenceIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = PrefixSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @org.hibernate.annotations.Parameter(name = PrefixSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "AP-"),
                    @org.hibernate.annotations.Parameter(name = PrefixSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d") })
    @Column(updatable = false)
    private String id;

    @Column(unique = true)
    private String title;

    @Builder.Default
    private DiplomaStatus status = DiplomaStatus.OPENED;

    @Column(updatable = false)
    private String ownerEmail;

    private String supervisorEmail;

    @Builder.Default
    @Column(updatable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @ElementCollection
    @CollectionTable(name = "diploma_application_tasks", joinColumns = @JoinColumn(name = "diploma_application_id"))
    @Column(name = "task")
    @Size(max = 10, message = "Tasks must not be more than 10")
    private List<@Size(max = 100) String> tasks;

    @ElementCollection
    @CollectionTable(name = "diploma_application_tech_stack", joinColumns = @JoinColumn(name = "diploma_application_id"))
    @Column(name = "tech_stack")
    @Size(max = 20, message = "Tech stack must not be more than 20")
    private List<@Size(max = 100) String> techStack;
}
