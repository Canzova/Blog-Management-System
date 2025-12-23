package com.blogManagementSystem.repository;

import com.blogManagementSystem.entity.Blog;
import com.blogManagementSystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    // findByIdAndAuthor ---> findBy --> id And author
    Optional<Blog> findByBlogIdAndAuthor(Long blogId, User user);

    Page<Blog> findByAuthor(User user, Pageable pageDetails);


    // Author_UserId ---> author.userId
    //Optional<Blog> findByIdAndAuthor_UserId(Long blogId, Long userId);
}

