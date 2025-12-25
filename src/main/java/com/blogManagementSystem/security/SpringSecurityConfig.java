package com.blogManagementSystem.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtFilter jwtFilter;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final AuthenticationErrorHandler authenticationErrorHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

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
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationErrorHandler)
                                .accessDeniedHandler(customAccessDeniedHandler)
                )
                .oauth2Login(httpSecurityOAuth2LoginConfigurer ->
                        httpSecurityOAuth2LoginConfigurer.successHandler(oAuth2SuccessHandler)
                                .failureHandler((request, response, exception) ->
                                        handlerExceptionResolver.resolveException(request, response, null, exception)));

        return httpSecurity.build();
    }
}
