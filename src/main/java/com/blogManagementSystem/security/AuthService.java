package com.blogManagementSystem.security;

import com.blogManagementSystem.dto.LoginRequestDTO;
import com.blogManagementSystem.dto.LoginResponseDTO;
import com.blogManagementSystem.dto.SignUpRequestDTO;
import com.blogManagementSystem.dto.SignUpResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AuthService {
    SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO);

    LoginResponseDTO login(@Valid LoginRequestDTO loginRequestDTO);

    ResponseEntity<LoginResponseDTO> handleOAuth2LoginRequest(OAuth2User user, OAuth2AuthenticationToken oAuth2Token, String registrationId);
}
