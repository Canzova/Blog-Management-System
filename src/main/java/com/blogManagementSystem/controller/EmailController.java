package com.blogManagementSystem.controller;

import com.blogManagementSystem.dto.EmailVerificationResponseDTO;
import com.blogManagementSystem.service.EmailVerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/verify")
@RequiredArgsConstructor
public class EmailController {

    private final EmailVerificationTokenService emailVerificationTokenService;

    @GetMapping("/verify")
    public ResponseEntity<EmailVerificationResponseDTO> verifyEmailVerificationToken(@RequestParam String token){
        EmailVerificationResponseDTO response = emailVerificationTokenService.verifyEmailVerificationToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
