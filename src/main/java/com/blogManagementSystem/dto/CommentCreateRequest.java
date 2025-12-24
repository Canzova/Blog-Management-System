package com.blogManagementSystem.dto;

import com.blogManagementSystem.entity.Blog;
import com.blogManagementSystem.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequest {

    @NotBlank(message = "Comment can't be null or blank")
    @Size(min = 3, max=20, message = "Comment should have at least 3 characters and at max 20 characters")
    private String comment;

}
