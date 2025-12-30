package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.CommentCreateRequest;
import com.blogManagementSystem.dto.CommentCreateResponse;
import com.blogManagementSystem.entity.Blog;
import com.blogManagementSystem.entity.Comment;
import com.blogManagementSystem.exception.ResourceNotFoundException;
import com.blogManagementSystem.repository.BlogRepository;
import com.blogManagementSystem.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;
    private final EmailService emailService;


    @Override
    @PreAuthorize("hasAuthority('comment:write') and authentication.principal.verified == true")
    @Transactional
    public CommentCreateResponse addComment(Long blogId, Long userId, CommentCreateRequest commentCreateRequestDTO) {
        // Note : This user will always exist in db because this is a logged-in user

        // Step 1 : Check if this blog exist or not
        Blog blog = blogRepository.findById(blogId).
                orElseThrow(()-> new ResourceNotFoundException("Blog", blogId));

        // Step 2 : Convert the commentDTO into comment entity
        Comment userComment = modelMapper.map(commentCreateRequestDTO, Comment.class);
        userComment.setBlog(blog);
        userComment.setUser(blog.getAuthor());

        //Comment savedComment = commentRepository.save(userComment);

        blog.getComments().add(userComment);

        // I am doing flush before converting userComment into DTO so that userComment is saved first and id and timestamp is created
        // After that only I want to convert it into DTO
        blogRepository.flush();


        // Send a notification to the user that someone has commented on your blog with that comment
        String subject = "💬 New Comment on Your Blog Post!";

        String body =
                "Hi 👋,\n\n" +
                        "Great news! Someone has just commented on your blog post 📝✨\n\n" +
                        "📌 Post Title:\n" +
                         "\n" + blog.getHeading()+ "\n\n" +
                        "💬 New Comment:\n" +
                        userComment.getComment() +
                        "👉 View your post here:\n" +
                        "http://localhost:8080/blog/" + blog.getBlogId() + "\n\n" +
                        "Keep writing amazing content 🚀\n\n" +
                        "Best regards,\n" +
                        "Blog Management System Team 💙";

        emailService.sendEmailNotification(body, blog.getAuthor().getUserEmail(), subject);

        return modelMapper.map(userComment, CommentCreateResponse.class);
    }

    @Override
    @PreAuthorize("hasAuthority('comment:edit') and authentication.principal.verified == true")
    @Transactional
    public CommentCreateResponse editComment(Long commentId, Long userId, CommentCreateRequest commentCreateRequestDTO) {
        // Step 1 : Check this comment id belongs to this user or not
        Comment userComment = commentRepository.findByCommentIdAndUser_UserId(commentId, userId).
                orElseThrow(()-> new ResourceNotFoundException("Comment", commentId, "User", userId));

        userComment.setComment(commentCreateRequestDTO.getComment()); // Dirty
        return modelMapper.map(userComment, CommentCreateResponse.class);
    }

    @Override
    @PreAuthorize("hasAuthority('comment:delete') and authentication.principal.verified == true")
    public CommentCreateResponse deleteComment(Long commentId, Long userId) {
        // Step 1 : Get this comment corresponding to this user
        Comment userComment = commentRepository.findByCommentIdAndUser_UserId(commentId, userId).
                orElseThrow(()-> new ResourceNotFoundException("Comment" , commentId, "User", userId));

        // Step 2 : Delete this Comment
        commentRepository.delete(userComment);
        return modelMapper.map(userComment, CommentCreateResponse.class);
    }
}
