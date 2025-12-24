package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.BlogCreateResponseDTO;
import com.blogManagementSystem.dto.BlogListResponse;

public interface UserService {


    BlogListResponse getMyBlogs(Long userId, String sortBy, String sortOrder, Integer pageNo, Integer pageSize);

    BlogCreateResponseDTO likeBlogByBlogId(Long blogId, Long userId);

    BlogCreateResponseDTO unLikeBlogByBlogId(Long blogId, Long userId);
}
