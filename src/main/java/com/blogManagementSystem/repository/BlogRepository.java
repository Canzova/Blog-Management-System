package com.blogManagementSystem.repository;

import com.blogManagementSystem.dto.constants.Category;
import com.blogManagementSystem.entity.Blog;
import com.blogManagementSystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    // findByIdAndAuthor ---> findBy --> id And author
    Optional<Blog> findByBlogIdAndAuthor(Long blogId, User user);

    Page<Blog> findByAuthor(User user, Pageable pageDetails);

    Page<Blog> findByCategory(Category enumCategory, Pageable pageable);

    Page<Blog> findByHeadingContaining(String heading, Pageable pageable);

    Optional<Blog> findByBlogIdAndAuthor_UserId(Long blogId, Long authorUserId);


    // Author_UserId ---> author.userId
    //Optional<Blog> findByIdAndAuthor_UserId(Long blogId, Long userId);
}

