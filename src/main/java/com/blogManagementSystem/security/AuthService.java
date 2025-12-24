package com.blogManagementSystem.security;

import com.blogManagementSystem.dto.LoginRequestDTO;
import com.blogManagementSystem.dto.LoginResponseDTO;
import com.blogManagementSystem.dto.SignUpRequestDTO;
import com.blogManagementSystem.dto.SignUpResponseDTO;
import jakarta.validation.Valid;

public interface AuthService {
    SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO);

    LoginResponseDTO login(@Valid LoginRequestDTO loginRequestDTO);
}
