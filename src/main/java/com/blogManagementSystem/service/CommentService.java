package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.CommentCreateRequest;
import com.blogManagementSystem.dto.CommentCreateResponse;

public interface CommentService {
    CommentCreateResponse addComment(Long blogId, Long userId, CommentCreateRequest commentCreateRequestDTO);

    CommentCreateResponse editComment(Long commentId, Long userId, CommentCreateRequest commentCreateRequestDTO);

    CommentCreateResponse deleteComment(Long commentId, Long userId);
}
