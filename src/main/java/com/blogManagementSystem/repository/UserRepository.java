package com.blogManagementSystem.repository;

import com.blogManagementSystem.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<UserDetails> findByUsername(String username);

    boolean existsByUsername(@NotBlank(message = "Password can't be blank or null") @Size(min = 3, message = "Username should have at least 3 characters") String username);
}
