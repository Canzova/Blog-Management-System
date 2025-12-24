package com.blogManagementSystem.controller;

import com.blogManagementSystem.dto.CommentCreateRequest;
import com.blogManagementSystem.dto.CommentCreateResponse;
import com.blogManagementSystem.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/add/{blogId}/{userId}")
    public ResponseEntity<CommentCreateResponse> addComment(@PathVariable Long blogId,
                                                            @PathVariable Long userId,
                                                            @RequestBody CommentCreateRequest commentCreateRequestDTO){

        CommentCreateResponse commentCreateResponse = commentService.addComment(blogId, userId, commentCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentCreateResponse);

    }

    @PutMapping("/edit/{commentId}/{userId}")
    public ResponseEntity<CommentCreateResponse> editComment(@PathVariable Long commentId,
                                                            @PathVariable Long userId,
                                                            @RequestBody CommentCreateRequest commentCreateRequestDTO){

        CommentCreateResponse commentCreateResponse = commentService.editComment(commentId, userId, commentCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentCreateResponse);

    }

    @DeleteMapping("delete/{commentId}/{userId}")
    public ResponseEntity<CommentCreateResponse> deleteComment(@PathVariable Long commentId,
                                                             @PathVariable Long userId){

        CommentCreateResponse commentCreateResponse = commentService.deleteComment(commentId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentCreateResponse);

    }
}
