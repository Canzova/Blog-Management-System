package com.blogManagementSystem.controller;

import com.blogManagementSystem.dto.DeletedUserDTO;
import com.blogManagementSystem.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @DeleteMapping("/delete/user/{userId}")
    public ResponseEntity<DeletedUserDTO> deleteUserByUserId(@PathVariable Long userId){
        DeletedUserDTO deletedUserDTO = adminService.deleteUserByUserId(userId);
        return new ResponseEntity<>(deletedUserDTO, HttpStatus.OK);
    }

}
