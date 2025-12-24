package com.blogManagementSystem.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf->csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(header-> header.frameOptions(
                        frameOptionsConfig -> frameOptionsConfig.sameOrigin()
                ))
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/sign-up/**", "login").permitAll()
                        .anyRequest().authenticated()
                );

        return httpSecurity.build();
    }
}
