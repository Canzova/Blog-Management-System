package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.BlogListResponse;

public interface UserService {


    BlogListResponse getMyBlogs(Long userId, String sortBy, String sortOrder, Integer pageNo, Integer pageSize);
}
