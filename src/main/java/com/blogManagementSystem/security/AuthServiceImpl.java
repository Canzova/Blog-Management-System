package com.blogManagementSystem.security;

import com.blogManagementSystem.dto.LoginRequestDTO;
import com.blogManagementSystem.dto.LoginResponseDTO;
import com.blogManagementSystem.dto.SignUpRequestDTO;
import com.blogManagementSystem.dto.SignUpResponseDTO;
import com.blogManagementSystem.dto.constants.ROLE;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.exception.GenericException;
import com.blogManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;

    @Override
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {
        // Step 1 :Check if user with name already exists
        if(userRepository.existsByUsername(signUpRequestDTO.getUsername())){
            throw new GenericException("Username already exists.");
        }

        // Step 2 : Now convert this DTO into entity
        User newUser = modelMapper.map(signUpRequestDTO, User.class);

        // Step 3 : Encrypt the password
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        // Step 3.1 : Also set the Role as user by default
        newUser.getRoles().add(ROLE.USER);

        // Step 4 : Now you can save this user into DB
        User savedUser = userRepository.save(newUser);

        return modelMapper.map(savedUser, SignUpResponseDTO.class);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String jwtToken = authUtil.generateJwtToken(user);

        return new LoginResponseDTO(user.getUserId(), user.getUsername(), jwtToken);
    }
}
