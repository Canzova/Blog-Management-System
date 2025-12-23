package com.blogManagementSystem.dto;

import com.blogManagementSystem.dto.constants.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogCreateRequestDTO {
    private String content;
    private Category category;
    private Boolean canComment;
}
