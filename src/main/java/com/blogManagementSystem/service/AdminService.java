package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.DeletedUserDTO;

public interface AdminService {
    DeletedUserDTO deleteUserByUserId(Long userId);
}
