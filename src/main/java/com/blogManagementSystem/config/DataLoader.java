package com.blogManagementSystem.config;

import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@Component
@Configuration
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        if (userRepository.count() > 0) {
            return; // prevent duplicate inserts on restart
        }

        User u1 = new User(
                null,
                "John",
                "Doe",
                "john@example.com",
                new ArrayList<>()
        );

        User u2 = new User(
                null,
                "Alice",
                "Smith",
                "alice@example.com",
                new ArrayList<>()
        );

        User u3 = new User(
                null,
                "Bob",
                "Brown",
                "bob@example.com",
                new ArrayList<>()
        );

        userRepository.saveAll(List.of(u1, u2, u3));
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
