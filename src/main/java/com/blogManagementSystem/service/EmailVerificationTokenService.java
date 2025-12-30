package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.EmailVerificationResponseDTO;
import com.blogManagementSystem.entity.User;

public interface EmailVerificationTokenService {
    void generateVerificationToken(User savedUser);

    EmailVerificationResponseDTO verifyEmailVerificationToken(String token);
}
