package com.blogManagementSystem.security;

import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
            // Step 1 : Get the authorization header
            String authorizationHeader = request.getHeader("Authorization");

            // Step 2 : Check if authorization header is invalid, pass the request to next filter
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Step 3 : Get the token
            String token = authorizationHeader.split("Bearer ")[1];

            // Step 4 : Get the username from token
            String username = authUtil.getUsernameFromToken(token);

            // Step 5 : If username is not null means that jwt token was valid and also check if securityContextHolder does already not have that user object
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Step 5.1 : Get the user
                User user = userRepository.findByUsername(username).orElseThrow();

                // Step 5.2 : Store the user into UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );

                // Step 5.3 : Add this UsernamePasswordAuthenticationToken in to SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                // Step 5.4 : Now goto next filter
                filterChain.doFilter(request, response);
            }
//        }
//        catch(Exception e){
//            handlerExceptionResolver.resolveException(request, response,  null, e);
//        }
    }
}
