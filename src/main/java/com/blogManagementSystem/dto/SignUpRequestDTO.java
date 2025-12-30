package com.blogManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDTO {

    @NotBlank(message = "Password can't be blank or null")
    @Size(min = 3, message = "Username should have at least 3 characters")
    private String userEmail;

    @NotBlank(message = "First Name can't be blank or null")
    private String firstName;

    @NotBlank(message = "Last Name can't be blank or null")
    private String lastName;

    @NotBlank(message = "Password can't be blank or null")
    @Size(min = 3, message = "Password should have at least 3 characters")
    private String password;



}
