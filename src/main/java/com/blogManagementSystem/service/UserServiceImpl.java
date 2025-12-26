package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.BlogCreateResponseDTO;
import com.blogManagementSystem.dto.BlogListResponse;
import com.blogManagementSystem.dto.CommentCreateResponse;
import com.blogManagementSystem.entity.Blog;
import com.blogManagementSystem.entity.BlogLike;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.exception.EmptyResourceException;
import com.blogManagementSystem.exception.GenericException;
import com.blogManagementSystem.exception.ResourceNotFoundException;
import com.blogManagementSystem.repository.BlogLikeRepository;
import com.blogManagementSystem.repository.BlogRepository;
import com.blogManagementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;
    private final BlogLikeRepository blogLikeRepository;

    @Override
    @PreAuthorize("hasAuthority('blog:read')")
    public BlogListResponse getMyBlogs(Long userId, String sortBy, String sortOrder, Integer pageNo, Integer pageSize) {
        // Step 1 : Check if this user exits
        User user = userRepository.findById(userId).
                orElseThrow(()-> new ResourceNotFoundException("User", userId));

        // Step 2 : Now you know that use exits so make pagination setting
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNo, pageSize, sortByAndOrder);

        Page<Blog>page = blogRepository.findByAuthor(user, pageDetails);

        List<Blog> blogList = page.getContent();

        if(blogList.isEmpty()){
            throw new EmptyResourceException("Blog List");
        }


        List<BlogCreateResponseDTO> blogCreateResponseDTOList = blogList.stream()
                .map(blog -> {
                    BlogCreateResponseDTO blogDTO = modelMapper.map(blog, BlogCreateResponseDTO.class);
                    blogDTO.setLikeCount(blog.getLikes() != null ? (long)blog.getLikes().size() : 0);
                    blogDTO.setUserId(blog.getAuthor().getUserId());
                    return blogDTO;
                })
                .toList();

        BlogListResponse blogListResponse = BlogListResponse.builder()
                .content(blogCreateResponseDTOList)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .isLastPage(page.isLast())
                .pageNumber(page.getNumber())
                .build();

        return blogListResponse;
    }

    @Override
    @PreAuthorize("hasAuthority('blog:like')")
    @Transactional
    public BlogCreateResponseDTO likeBlogByBlogId(Long blogId, Long userId) {
        // Step 1 : Check if this blogId exits with this author (User)
        Blog blog = blogRepository.findById(blogId).
                orElseThrow(()-> new ResourceNotFoundException("Blog", blogId));

        // This user will always exist ---> Logged-in user
        User user = userRepository.findById(userId).
                orElseThrow();

        if(blogLikeRepository.existsByUser_UserIdAndBlog_BlogId (userId, blogId)) {
            throw new GenericException("You can like a post at-most once.");
        }

        BlogLike blogLike = BlogLike.builder()
                .user(user)
                .blog(blog)
                .build();

        blog.getLikes().add(blogLike); // Dirty --> Due to cascade.all blogLike will be automatically saved

        return getBlogCreateResponseDTO(blog);
    }

    @Override
    @PreAuthorize("hasAuthority('blog:unlike')")
    @Transactional
    public BlogCreateResponseDTO unLikeBlogByBlogId(Long blogId, Long userId) {
        // Step 1 : Check if this blogId exits with this author (User)
        Blog blog = blogRepository.findById(blogId).
                orElseThrow(()-> new ResourceNotFoundException("Blog", blogId));

        // This user will always exist
        User user = userRepository.findById(userId).
                orElseThrow();

        // What if user has not liked it before and now trying to unlike it
        BlogLike blogLike = blogLikeRepository.findByUser_UserIdAndBlog_BlogId(userId, blogId)
                .orElseThrow(()-> new GenericException("You haven't liked this post earlier."));

        // Delete this BlogLike
        blogLikeRepository.delete(blogLike);

        blog.getLikes().remove(blogLike); // Dirty

        return getBlogCreateResponseDTO(blog);
    }

    private BlogCreateResponseDTO getBlogCreateResponseDTO(Blog blog) {
        List<CommentCreateResponse> commentCreateResponseDTOList = blog.getComments().stream()
                .map(comment -> modelMapper.map(comment, CommentCreateResponse.class))
                .toList();

        BlogCreateResponseDTO blogCreateResponseDTO = modelMapper.map(blog, BlogCreateResponseDTO.class);

        blogCreateResponseDTO.setComments(commentCreateResponseDTOList);
        blogCreateResponseDTO.setUserId(blog.getAuthor().getUserId());
        blogCreateResponseDTO.setLikeCount(blog.getLikes() != null ? (long) blog.getLikes().size(): 0L);
        return blogCreateResponseDTO;
    }
}