package org.example.userservice.user.details.data.repository;

import org.example.userservice.user.details.data.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
}
