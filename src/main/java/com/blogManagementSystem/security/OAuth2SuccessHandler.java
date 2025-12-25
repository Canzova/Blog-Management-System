package com.blogManagementSystem.security;

import com.blogManagementSystem.dto.LoginResponseDTO;
import com.blogManagementSystem.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Step 1 : Get the OAuth2 Authentication token
        OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;

        // Step 2 : Get the OAuth2User from oAuth2Token
        OAuth2User user = (OAuth2User) oAuth2Token.getPrincipal();

        // Step 3 : Get the registration id
        String registrationId = oAuth2Token.getAuthorizedClientRegistrationId();

        // Step 4 : Handle the OAuth2LoginRequest
        ResponseEntity<LoginResponseDTO> loginResponse = authService.handleOAuth2LoginRequest(user, oAuth2Token, registrationId);

        // Step 5 : Add the loginResponse in to response
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(loginResponse.getStatusCode().value());
        //response.getWriter().write(objectMapper.writeValueAsString(loginResponse.getBody()));
        objectMapper.writeValue(response.getOutputStream(), loginResponse.getBody());
    }
}
