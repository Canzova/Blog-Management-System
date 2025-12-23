package com.blogManagementSystem.controller;

import com.blogManagementSystem.dto.BlogCreateRequestDTO;
import com.blogManagementSystem.dto.BlogCreateResponseDTO;
import com.blogManagementSystem.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @PostMapping("/write/{id}")
    public ResponseEntity<BlogCreateResponseDTO> writeBlog(@Valid @RequestBody BlogCreateRequestDTO blogCreateRequestDTO,
                                                           @PathVariable(name = "id") Long userId){
        // Logged In user ID
        BlogCreateResponseDTO blogCreateResponseDTO = blogService.writeBlog(userId, blogCreateRequestDTO);
        return new ResponseEntity<>(blogCreateResponseDTO, HttpStatus.CREATED);

    }

    @PutMapping("/edit/blog/{blogId}/{id}")
    public ResponseEntity<BlogCreateResponseDTO> editBlog(@Valid @RequestBody BlogCreateRequestDTO blogCreateRequestDTO,
                                                          @PathVariable(name = "id") Long userId,
                                                          @PathVariable Long blogId){
        // Logged In user ID
        BlogCreateResponseDTO blogCreateResponseDTO = blogService.editBlog(userId, blogId, blogCreateRequestDTO);
        return new ResponseEntity<>(blogCreateResponseDTO, HttpStatus.CREATED);

    }
}
