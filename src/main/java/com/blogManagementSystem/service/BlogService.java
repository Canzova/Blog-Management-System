package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.BlogCreateRequestDTO;
import com.blogManagementSystem.dto.BlogCreateResponseDTO;
import com.blogManagementSystem.dto.BlogListResponse;
import org.springframework.stereotype.Service;

@Service
public interface BlogService {
    public BlogCreateResponseDTO writeBlog(Long id, BlogCreateRequestDTO blogCreateRequestDTO);

    BlogCreateResponseDTO editBlog(Long userId, Long blogId, BlogCreateRequestDTO blogCreateRequestDTO);

    BlogListResponse getCategoryBlogs(String categoryName, Integer pageSize, Integer pageNumber, String sortOrder, String sortBy);

    BlogListResponse getBlogsByHeading(String heading, Integer pageSize, Integer pageNumber, String sortOrder, String sortBy);

    BlogListResponse getAllBlogs(Integer pageSize, Integer pageNumber, String sortOrder, String sortBy);

    BlogCreateResponseDTO deleteBlogById(Long userId, Long blogId);

    BlogCreateResponseDTO getBlogByBlogId(Long blogId);
}
