package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.DeletedUserDTO;
import com.blogManagementSystem.entity.Blog;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.exception.ResourceNotFoundException;
import com.blogManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('user:delete') and authentication.principal.verified == true")
    public DeletedUserDTO deleteUserByUserId(Long userId) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        // Now delete this user
        userRepository.delete(user);

        return modelMapper.map(user, DeletedUserDTO.class);

    }
}
