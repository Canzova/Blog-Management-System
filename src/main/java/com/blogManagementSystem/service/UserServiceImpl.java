package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.BlogCreateResponseDTO;
import com.blogManagementSystem.dto.BlogListResponse;
import com.blogManagementSystem.dto.CommentCreateResponse;
import com.blogManagementSystem.entity.Blog;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.exception.EmptyResourceException;
import com.blogManagementSystem.exception.GenericException;
import com.blogManagementSystem.exception.ResourceNotFoundException;
import com.blogManagementSystem.repository.BlogRepository;
import com.blogManagementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;

    @Override
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
    @Transactional
    public BlogCreateResponseDTO likeBlogByBlogId(Long blogId, Long userId) {
        // Step 1 : Check if this blogId exits with this author (User)
        Blog blog = blogRepository.findById(blogId).
                orElseThrow(()-> new ResourceNotFoundException("Blog", blogId));

        // This user will always exist ---> Logged-in user
        User user = userRepository.findById(userId).
                orElseThrow();

        blog.getLikes().add(user); // Dirty

        return getBlogCreateResponseDTO(blog);
    }

    @Override
    @Transactional
    public BlogCreateResponseDTO unLikeBlogByBlogId(Long blogId, Long userId) {
        // Step 1 : Check if this blogId exits with this author (User)
        Blog blog = blogRepository.findById(blogId).
                orElseThrow(()-> new ResourceNotFoundException("Blog", blogId));

        // This user will always exist
        User user = userRepository.findById(userId).
                orElseThrow();

        // What if user has not liked it before and now trying to unlike it
        if(!blog.getLikes().contains(user)) throw new GenericException("You haven't liked this post earlier.");

        blog.getLikes().remove(user); // Dirty

        return getBlogCreateResponseDTO(blog);
    }

    private BlogCreateResponseDTO getBlogCreateResponseDTO(Blog blog) {
        List<CommentCreateResponse> commentCreateResponseDTOList = blog.getComments().stream()
                .map(comment -> modelMapper.map(comment, CommentCreateResponse.class))
                .toList();

        BlogCreateResponseDTO blogCreateResponseDTO = modelMapper.map(blog, BlogCreateResponseDTO.class);

        blogCreateResponseDTO.setComments(commentCreateResponseDTOList);
        blogCreateResponseDTO.setLikeCount(blog.getLikes() != null ? (long) blog.getLikes().size(): 0L);
        return blogCreateResponseDTO;
    }
}