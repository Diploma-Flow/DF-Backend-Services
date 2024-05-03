package org.example.userservice.repository;

import org.example.userservice.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
}
