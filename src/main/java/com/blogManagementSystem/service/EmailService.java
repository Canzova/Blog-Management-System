package com.blogManagementSystem.service;

import jakarta.validation.constraints.Email;

public interface EmailService {
    void sendEmailNotification(String body, @Email String username, String subject);

}
