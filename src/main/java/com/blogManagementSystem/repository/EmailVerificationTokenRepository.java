package com.blogManagementSystem.repository;

import com.blogManagementSystem.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    boolean existsByVerificationToken(String token);

    EmailVerificationToken findByVerificationToken(String token);

    void deleteByVerificationToken(String token);
}
