package com.blogManagementSystem.repository;

import com.blogManagementSystem.dto.constants.AuthProviderType;
import com.blogManagementSystem.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String username);

    boolean existsByUserEmail(@NotBlank(message = "Password can't be blank or null") @Size(min = 3, message = "Username should have at least 3 characters") String username);

    User findByAuthProviderTypeAndProviderId(AuthProviderType authProviderType, String providerId);
}
