package com.blogManagementSystem.controller;

import com.blogManagementSystem.dto.LoginRequestDTO;
import com.blogManagementSystem.dto.LoginResponseDTO;
import com.blogManagementSystem.dto.SignUpRequestDTO;
import com.blogManagementSystem.dto.SignUpResponseDTO;
import com.blogManagementSystem.security.AuthService;
import com.blogManagementSystem.security.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDTO> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO){
        SignUpResponseDTO signUpResponseDTO = authService.signUp(signUpRequestDTO);
        return new ResponseEntity<>(signUpResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
        return new ResponseEntity<>(loginResponseDTO, HttpStatus.CREATED);
    }

}
