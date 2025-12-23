package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.BlogCreateRequestDTO;
import com.blogManagementSystem.dto.BlogCreateResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface BlogService {
    public BlogCreateResponseDTO writeBlog(Long id, BlogCreateRequestDTO blogCreateRequestDTO);

    BlogCreateResponseDTO editBlog(Long userId, Long blogId, BlogCreateRequestDTO blogCreateRequestDTO);
}
