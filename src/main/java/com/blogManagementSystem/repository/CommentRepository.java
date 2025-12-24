package com.blogManagementSystem.repository;

import com.blogManagementSystem.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment>findByCommentIdAndUser_UserId(Long commentId, Long userId);
}
