package org.example.userservice.repository;

import org.example.userservice.enums.UserRole;
import org.example.userservice.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByEmail(String email);

    List<UserAccount> findByUsernameStartingWith(String usernamePrefix);

    List<UserAccount> findUserAccountsByUsernameStartingWith(String usernamePrefix);

    Page<UserAccount> findByRole(UserRole role, Pageable pageable);
}

