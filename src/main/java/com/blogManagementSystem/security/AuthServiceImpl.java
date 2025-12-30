package com.blogManagementSystem.security;

import com.blogManagementSystem.dto.LoginRequestDTO;
import com.blogManagementSystem.dto.LoginResponseDTO;
import com.blogManagementSystem.dto.SignUpRequestDTO;
import com.blogManagementSystem.dto.SignUpResponseDTO;
import com.blogManagementSystem.dto.constants.AuthProviderType;
import com.blogManagementSystem.dto.constants.ROLE;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.exception.EmailVerificationTokenException;
import com.blogManagementSystem.exception.GenericException;
import com.blogManagementSystem.repository.UserRepository;
import com.blogManagementSystem.service.EmailVerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final EmailVerificationTokenService emailVerificationTokenService;

    public User signUpInternal(SignUpRequestDTO signUpRequestDTO, String providerId, AuthProviderType providerType){
        // Step 1 :Check if user with name already exists
        if(userRepository.existsByUserEmail(signUpRequestDTO.getUserEmail())){
            throw new GenericException("Username already exists.");
        }

        // Step 2 : Now convert this DTO into entity
        User newUser = modelMapper.map(signUpRequestDTO, User.class);

        // Step 3 : Encrypt the password
        if(signUpRequestDTO.getPassword() != null) newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        // Check if providerId and providerType exists ---> Save them
        if(providerId != null) newUser.setProviderId(providerId);
        if(providerType != null) newUser.setAuthProviderType(providerType);

        // Step 3.1 : Also set the Role as user by default
        newUser.getRoles().add(ROLE.USER);

        // Step 3.2 : Set the verification status as false
        newUser.setVerified(false);

        // Step 4 : Now you can save this user into DB
        User savedUser =  userRepository.save(newUser);

        // Step 5 : Now generate one emailVerificationToken and mail it to the user
        emailVerificationTokenService.generateVerificationToken(savedUser);

        return savedUser;
    }


    @Override
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {
        return modelMapper.map(signUpInternal(signUpRequestDTO, null, AuthProviderType.EMAIL), SignUpResponseDTO.class);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUserEmail(), loginRequestDTO.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        // Make sure that user is verified before login
        if(user.getVerified() == false) throw new EmailVerificationTokenException("Please verify your email first.");
        String jwtToken = authUtil.generateJwtToken(user);

        return new LoginResponseDTO(user.getUserId(), user.getUserEmail(), jwtToken);
    }

    @Override
    public ResponseEntity<LoginResponseDTO> handleOAuth2LoginRequest(OAuth2User user, OAuth2AuthenticationToken oAuth2Token, String registrationId) {
        // Step 1 : Get the AuthProvider type
        AuthProviderType providerType = authUtil.getAuthProviderTypeFromRegistrationId(registrationId);

        // Step 2 : Get the Provider id
        String providerId = authUtil.getProviderIdFromOAuth2UserAndRegistrationId(user, registrationId);

        // Step 3 : get the user form UserRepo with this providerType and providerId
        User oAuth2User = userRepository.findByAuthProviderTypeAndProviderId(providerType, providerId);

        // Step 4 : Get the name and email of this user
        String name = user.getAttribute("name");
        String email = user.getAttribute("email");

        // Step 5 : Get the user with this email
        User userWithEmail = userRepository.findByUserEmail(email).orElse(null);

        String firstName = null;
        String lastName = null;

        if ("google".equals(registrationId)) {
            firstName = user.getAttribute("given_name");
            lastName = user.getAttribute("family_name");
        }
        else if(name != null){
            String[] parts = name.split(" ");
            firstName = parts[0];

            if(parts.length > 1) lastName = parts[1];
        }


        if(oAuth2User == null && userWithEmail == null){
            // This user is new ---> Signup and Login
            String userEmail = authUtil.getUserEmailFromOAuth2UserAndRegistrationId(user, registrationId, providerId);
            oAuth2User = signUpInternal(new SignUpRequestDTO(userEmail, firstName, lastName, null), providerId, providerType);
        }
        else if(oAuth2User != null){
            // This user has already done signup with this same OAuth2 Provider, just check mail and do login
            if(email != null && !email.isBlank() && !email.equals(oAuth2User.getUserEmail())){
                oAuth2User.setUserEmail(email);
                userRepository.save(oAuth2User);
            }
        }
        else {
            // User has previously logged-in using any other OAuth2Provider
            throw new BadCredentialsException("This email is already registered with provider "+userWithEmail.getProviderId());
        }

        return ResponseEntity.ok().
                body(new LoginResponseDTO(oAuth2User.getUserId(), oAuth2User.getUserEmail(), authUtil.generateJwtToken(oAuth2User)));
    }
}
