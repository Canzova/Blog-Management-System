package com.blogManagementSystem.ServiceTest;

import com.blogManagementSystem.dto.BlogCreateResponseDTO;
import com.blogManagementSystem.dto.BlogListResponse;
import com.blogManagementSystem.dto.constants.Category;
import com.blogManagementSystem.entity.Blog;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.exception.EmptyResourceException;
import com.blogManagementSystem.exception.GenericException;
import com.blogManagementSystem.exception.ResourceNotFoundException;
import com.blogManagementSystem.repository.BlogLikeRepository;
import com.blogManagementSystem.repository.BlogRepository;
import com.blogManagementSystem.repository.UserRepository;
import com.blogManagementSystem.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BlogRepository blogRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private BlogLikeRepository blogLikeRepository;


    @InjectMocks
    private UserServiceImpl userService;

    private static Long userId;
    private static String sortOrder;
    private static String sortBy;
    private static Integer pageNo;
    private static Integer pageSize;
    private static Long blogId;

    private User user;
    private Page<Blog> page;
    private static Pageable pageDetails;
    private List<Blog>blogs = new ArrayList<>();

    @BeforeAll
    public static void init(){
        userId = 1L;
        blogId = 1L;
        sortOrder = "asc";
        sortBy = "blogId";
        pageNo = 0;
        pageSize = 2;

        Sort sortByAndOrder = Sort.by(sortBy).ascending();

        pageDetails = PageRequest.of(pageNo, pageSize, sortByAndOrder);
    }

    @BeforeEach
    public void initBeforeEach(){
        blogs.clear();
        user = new User();
        user.setUserId(1L);
        user.setUserEmail("temp@gmail.com");

        blogs.add(Blog.builder().blogId(1L).comments(List.of()).canComment(true).heading("First Blog").
                author(user).content("Hii their").category(Category.SCIENCE).likes(new HashSet<>()).build());
        blogs.add(Blog.builder().blogId(2L).comments(List.of()).canComment(true).heading("Second Blog").
                author(user).content("Hii").category(Category.SCIENCE).likes(new HashSet<>()).build());
        blogs.add(Blog.builder().blogId(3L).comments(List.of()).canComment(true).heading("Third Blog").
                author(user).content("Hello").category(Category.SCIENCE).likes(new HashSet<>()).build());

        page = new PageImpl<Blog>(
                blogs,
                pageDetails,
                blogs.size()
        );
    }

    @Test
    public void getMyBlogs_shouldReturnBlogListResponse() {

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito.when(blogRepository.findByAuthor(Mockito.eq(user), Mockito.any(Pageable.class)))
                .thenReturn(page);

        Mockito.when(modelMapper.map(Mockito.any(Blog.class), Mockito.eq(BlogCreateResponseDTO.class)))
                .thenAnswer(invocation -> {
                    Blog blog = invocation.getArgument(0);
                    BlogCreateResponseDTO dto = new BlogCreateResponseDTO();
                    dto.setBlogId(blog.getBlogId());
                    dto.setHeading(blog.getHeading());
                    return dto;
                });

        BlogListResponse response =
                userService.getMyBlogs(userId, sortBy, sortOrder, pageNo, pageSize);

        // ✅ Assertions
        assertNotNull(response);
        assertEquals(3, response.getContent().size());
        assertEquals(0, response.getPageNumber());
        assertEquals(blogs.size(), response.getTotalElements());
        assertFalse(response.getIsLastPage());
        assertEquals(userId, response.getContent().getFirst().getUserId());
    }

    @Test
    public void getMyBlogs_shouldReturnResourceNotFoundException(){

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(
                ResourceNotFoundException.class, ()-> userService.getMyBlogs(userId, sortBy, sortOrder, pageNo, pageSize)
        );

        Assertions.assertEquals("User not found with id 1", resourceNotFoundException.getMessage());
    }

    @Test
    public void getMyBlogs_shouldReturnEmptyResourceException(){
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        page = new PageImpl<Blog>(
                List.of(),
                pageDetails,
                0
        );

        Mockito.when(blogRepository.findByAuthor(user, pageDetails)).thenReturn(page);

        EmptyResourceException emptyResourceException = Assertions.assertThrows(
                EmptyResourceException.class,
                ()-> userService.getMyBlogs(userId, sortBy, sortOrder, pageNo, pageSize)
        );

        Assertions.assertEquals("Blog List is empty.", emptyResourceException.getMessage());
    }

    @Test
    public void likeBlogByBlogId_shouldReturnLikedBlogDTO(){

        Mockito.when(blogRepository.findById(blogId)).thenReturn(Optional.ofNullable(blogs.getFirst()));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        Mockito.when(blogLikeRepository.existsByUser_UserIdAndBlog_BlogId (userId, blogId)).thenReturn(false);

        Mockito.when(modelMapper.map(blogs.getFirst(), BlogCreateResponseDTO.class)).thenReturn(
               BlogCreateResponseDTO.builder().
                        blogId(blogs.getFirst().getBlogId()).
                        heading(blogs.getFirst().getHeading()).
                        content(blogs.getFirst().getContent()).
                        category(blogs.getFirst().getCategory()).
                        publishDateTime(blogs.getFirst().getPublishDateTime()).
                        canComment(blogs.getFirst().getCanComment()).
                        userId(blogs.getFirst().getAuthor().getUserId()).
                        build()
        );

        BlogCreateResponseDTO blogCreateResponseDTO = userService.likeBlogByBlogId(blogId, userId);
        Assertions.assertEquals(blogs.getFirst().getBlogId(), blogCreateResponseDTO.getBlogId());
    }

    @Test
    public void likeBlogByBlogId_shouldThrowResourceNotFoundException(){
        Mockito.when(blogRepository.findById(blogId)).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(
                ResourceNotFoundException.class,
                ()-> userService.likeBlogByBlogId(blogId, userId)
        );

        Assertions.assertEquals("Blog not found with id 1", resourceNotFoundException.getMessage());
    }

    @Test
    public void likedBlogByBlogId_shouldThrowGenericException(){
        Mockito.when(blogRepository.findById(blogId)).thenReturn(Optional.ofNullable(blogs.getFirst()));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        Mockito.when(blogLikeRepository.existsByUser_UserIdAndBlog_BlogId (userId, blogId)).thenReturn(true);

        GenericException genericException = Assertions.assertThrows(
                GenericException.class,
                ()-> userService.likeBlogByBlogId(blogId, userId)
        );

        Assertions.assertEquals("You can like a post at-most once.", genericException.getMessage());
    }

}
