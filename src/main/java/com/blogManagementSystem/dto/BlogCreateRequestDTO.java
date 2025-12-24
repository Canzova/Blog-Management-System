package com.blogManagementSystem.dto;

import com.blogManagementSystem.dto.constants.Category;
import com.blogManagementSystem.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogCreateRequestDTO {

    @NotBlank(message = "Blog cannot be blank or null")
    @Size(min=10, max=1000, message = "Blog should be at-least 10 characters long and at-max 1000 characters long.")
    private String content;

    // This has to be an enum
    @NotNull(message = "Category cannot be null")
    private Category category;

    private Boolean canComment;

    @NotBlank(message = "Blog's Heading cannot be blank or null")
    @Size(min=10, max=1000, message = "Blog's Heading should be at-least 5 characters long")
    private String heading;

}
