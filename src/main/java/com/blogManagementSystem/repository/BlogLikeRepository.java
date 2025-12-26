package com.blogManagementSystem.repository;

import com.blogManagementSystem.entity.BlogLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogLikeRepository extends JpaRepository<BlogLike, Long> {
    Optional<BlogLike> findByUser_UserIdAndBlog_BlogId(Long userId, Long blogId);

    boolean existsByUser_UserIdAndBlog_BlogId(Long userId, Long blogId);
}
