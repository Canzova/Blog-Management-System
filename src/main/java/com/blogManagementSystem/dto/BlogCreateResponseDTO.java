package com.blogManagementSystem.dto;

import com.blogManagementSystem.dto.constants.Category;
import com.blogManagementSystem.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogCreateResponseDTO {
    private Long blogId;
    private String heading;
    private String content;
    private Category category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishDateTime;

    private Boolean canComment;
    private Long authorId;
    private Long likeCount;

    private List<CommentCreateResponse> comments = new ArrayList<>();
}
