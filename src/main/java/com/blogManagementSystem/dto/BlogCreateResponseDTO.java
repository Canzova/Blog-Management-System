package com.blogManagementSystem.dto;

import com.blogManagementSystem.dto.constants.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogCreateResponseDTO {
    private Long blogId;
    private String content;
    private Category category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishDateTime;

    private Boolean canComment;
    private Long authorId;
    private Long likeCount;
}
