package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.EmailVerificationResponseDTO;
import com.blogManagementSystem.entity.EmailVerificationToken;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.exception.EmailVerificationTokenException;
import com.blogManagementSystem.repository.EmailVerificationTokenRepository;
import com.blogManagementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService{
    private final EmailService emailService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserRepository userRepository;

    @Override
    public void generateVerificationToken(User savedUser) {

        // Step 1 : Generate a new Token for user
        EmailVerificationToken token = EmailVerificationToken.builder()
                .user(savedUser)
                .verificationToken(UUID.randomUUID().toString())
                .expirationTime(LocalTime.now().plusMinutes(15))
                .build();

        // Step 2 : Save into dp
        emailVerificationTokenRepository.save(token);

        // Step 3 : With this token send a mail to user
        String subject = "✅ Please Verify Your Email Address";

        String body =
                "Hi " + savedUser.getFirstName() + " 👋,\n\n" +
                        "Welcome to Blog Management System! 🎉\n\n" +
                        "Thank you for signing up. To complete your registration and secure your account, " +
                        "please verify your email address by clicking the link below 👇\n\n" +
                        "🔐 Verify your email:\n" +
                        "http://localhost:8080/verify?token=" + token.getVerificationToken() + "\n\n" +
                        "⏰ This link will expire in 15 minutes.\n\n" +
                        "If you did not create this account, you can safely ignore this email.\n\n" +
                        "We’re excited to have you with us 🚀\n\n" +
                        "Best regards,\n" +
                        "Blog Management System Team 💙";

        emailService.sendEmailNotification(body, savedUser.getUserEmail(), subject);
    }

    @Transactional
    @Override
    public EmailVerificationResponseDTO verifyEmailVerificationToken(String token) {
        // Step 1 : Check if this token exists
        if(!emailVerificationTokenRepository.existsByVerificationToken(token))
            throw new EmailVerificationTokenException("Verification Token does not exists.");

        // Step 2 : Get the token from db
        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByVerificationToken(token);

        // Step 3 : Check the expirationTime
        if(emailVerificationToken.getExpirationTime().isBefore(LocalTime.now())){
            throw new EmailVerificationTokenException("Verification Token expired.");
        }

        // Step 4 : Every thing is alright so now mark user as verified
        User loggedInUser = emailVerificationToken.getUser();
        loggedInUser.setVerified(true);
        userRepository.save(loggedInUser);

        // Step 5 : Now this token is of no use so delete it ---> Delete query must be inside a transactional
        emailVerificationTokenRepository.deleteByVerificationToken(token);

        // Step 6 : Return the response
        return new EmailVerificationResponseDTO("Verified Successfully!");
    }
}
