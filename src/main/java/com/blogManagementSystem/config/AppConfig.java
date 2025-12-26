package com.blogManagementSystem.config;

import com.blogManagementSystem.dto.constants.AuthProviderType;
import com.blogManagementSystem.dto.constants.ROLE;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//@Component
@Configuration
public class AppConfig implements CommandLineRunner {

    private final UserRepository userRepository;

    public AppConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        if (userRepository.count() > 0) {
            return; // prevent duplicate inserts on restart
        }

        User u1 = new User(
                null,
                "user1@gmail.com",
                passwordEncoder().encode("pass"),
                "John",
                "Doe",
                Set.of(ROLE.ADMIN),
                AuthProviderType.EMAIL,
                "ADMIN",
                new ArrayList<>()
        );

        User u2 = new User(
                null,
                "user2@gmail.com",
                passwordEncoder().encode("pass"),
                "Alice",
                "Smith",
                Set.of(ROLE.ADMIN),
                AuthProviderType.EMAIL,
                "ADMIN",
                new ArrayList<>()
        );

        User u3 = new User(
                null,
                "user3@gmail.com",
                passwordEncoder().encode("pass"),
                "Bob",
                "Brown",
                Set.of(ROLE.ADMIN),
                AuthProviderType.EMAIL,
                "ADMIN",
                new ArrayList<>()
        );

        userRepository.saveAll(List.of(u1, u2, u3));
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
