package org.example.userservice.user.account.data.repository;

import org.example.userservice.user.account.data.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
