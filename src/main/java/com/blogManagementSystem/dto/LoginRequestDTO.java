package com.blogManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "User Email can't be blank or null")
    @Size(min = 3, message = "User Email should have at least 3 characters")
    private String userEmail;

    @NotBlank(message = "Password can't be blank or null")
    @Size(min = 3, message = "Password should have at least 3 characters")
    private String password;

}
