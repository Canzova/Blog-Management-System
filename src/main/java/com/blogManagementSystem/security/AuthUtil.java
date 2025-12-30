package com.blogManagementSystem.security;

import com.blogManagementSystem.dto.constants.AuthProviderType;
import com.blogManagementSystem.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class AuthUtil {

    @Value("${spring.jwt.key}")
    private String jwtSecretKey;

    // Step 1 : Generate Cryptographic SecretKey key
    private SecretKey generateSecretKey(){
        return Keys.hmacShaKeyFor(
                jwtSecretKey.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateJwtToken(User user) {
        return Jwts.builder()
                .subject(user.getUserEmail())
                .claim("userId", user.getUserId())
                .claim("userFirstName", user.getFirstName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(generateSecretKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(generateSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public AuthProviderType getAuthProviderTypeFromRegistrationId(String registrationId) {
        return switch(registrationId.toLowerCase()){
            case "google" -> AuthProviderType.GOOGLE;
            case "github" -> AuthProviderType.GITHUB;

            default -> throw new IllegalStateException("Unsupported OAuth2 provider " + registrationId);
        };
    }

    public String getProviderIdFromOAuth2UserAndRegistrationId(OAuth2User user, String registrationId) {
        String providerId = switch(registrationId.toLowerCase()){
            case "google" -> user.getAttribute("sub");
            case "github" -> user.getAttribute("id").toString();
            default -> throw new IllegalStateException("Unsupported OAuth2 provider " + registrationId);
        };

        // What if in the user inside id/sub you get a null value
        if(providerId == null){
            throw new IllegalArgumentException("Unable to determine providerId for OAuth2 login");
        }

        return providerId;
    }

    public String getUserEmailFromOAuth2UserAndRegistrationId(OAuth2User user, String registrationId, String providerId) {
        String userName = user.getAttribute("email");
        if(userName != null && !userName.isBlank()) return userName;

        return switch (registrationId.toLowerCase()){
            case "google" -> {
                if(providerId != null) yield providerId + AuthProviderType.GOOGLE.name();
                yield null;
            }
            case "github" -> {
                if(providerId != null) yield providerId + AuthProviderType.GITHUB.name();
                yield null;
            }
            default -> providerId;   // If provider is unknown just return thr providerId
        };
    }
}
