package org.simo.dms.diplomamanagementservice.repository;

import org.simo.dms.diplomamanagementservice.model.DiplomaApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * Author: Simeon Popov
 * Date of creation: 6/19/2024
 */
@Repository
public interface DiplomaApplicationRepository extends JpaRepository<DiplomaApplication,String> {

    Optional<DiplomaApplication> findByOwnerEmail (String email);

    List<DiplomaApplication> findAllByOwnerEmail(String ownerEmail);
}

