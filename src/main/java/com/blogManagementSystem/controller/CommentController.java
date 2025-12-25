package com.blogManagementSystem.controller;

import com.blogManagementSystem.dto.CommentCreateRequest;
import com.blogManagementSystem.dto.CommentCreateResponse;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/add/{blogId}")
    public ResponseEntity<CommentCreateResponse> addComment(@PathVariable Long blogId,
                                                            @RequestBody CommentCreateRequest commentCreateRequestDTO){
        // Logged-in user
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CommentCreateResponse commentCreateResponse = commentService.addComment(blogId, loggedInUser.getUserId(), commentCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentCreateResponse);

    }

    @PutMapping("/edit/{commentId}")
    public ResponseEntity<CommentCreateResponse> editComment(@PathVariable Long commentId,
                                                            @RequestBody CommentCreateRequest commentCreateRequestDTO){
        // Logged-in user
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CommentCreateResponse commentCreateResponse = commentService.editComment(commentId, loggedInUser.getUserId(), commentCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentCreateResponse);

    }

    @DeleteMapping("delete/{commentId}")
    public ResponseEntity<CommentCreateResponse> deleteComment(@PathVariable Long commentId){
        // Logged-in user
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CommentCreateResponse commentCreateResponse = commentService.deleteComment(commentId, loggedInUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentCreateResponse);

    }
}
