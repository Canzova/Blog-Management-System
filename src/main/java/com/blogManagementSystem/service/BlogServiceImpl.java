package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.BlogCreateRequestDTO;
import com.blogManagementSystem.dto.BlogCreateResponseDTO;
import com.blogManagementSystem.dto.BlogListResponse;
import com.blogManagementSystem.dto.CommentCreateResponse;
import com.blogManagementSystem.dto.constants.Category;
import com.blogManagementSystem.entity.Blog;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.exception.EmptyResourceException;
import com.blogManagementSystem.exception.ResourceNotFoundException;
import com.blogManagementSystem.repository.BlogRepository;
import com.blogManagementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BlogRepository blogRepository;

    @Override
    @Transactional
    public BlogCreateResponseDTO writeBlog(Long id, BlogCreateRequestDTO blogCreateRequestDTO) {
        // Step 1 : Check if ths user exists in db and get it ---> Edit : USer will always exist because it is logged-in user
        User user = userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("User", id));

        // User is now stored in persistence context
        Blog blog = Blog.builder()
                .content(blogCreateRequestDTO.getContent())
                .category(blogCreateRequestDTO.getCategory())
                .canComment(blogCreateRequestDTO.getCanComment())
                .heading(blogCreateRequestDTO.getHeading())
                .author(user)
                .comments(List.of()) // Putting an empty list instead of null
                .build();

        user.getBlog().add(blog);  // user is dirty now remember
        /*
            I am just doing flush before converting my blog into BlogResponseDTO so that after saving when publishDateTime
            is initialized then i will convert it into BlogResponseDTO

         */
        userRepository.flush(); // forces INSERT

        BlogCreateResponseDTO response = modelMapper.map(blog, BlogCreateResponseDTO.class);
        response.setAuthorId(id);
        response.setLikeCount(blog.getLikes() != null ? (long) blog.getLikes().size() : 0L);
        return response;
    }

    @Override
    @Transactional
    public BlogCreateResponseDTO editBlog(Long userId, Long blogId, BlogCreateRequestDTO blogCreateRequestDTO) {

        // Step 1 : Check if ths user exists in db and get it ---> Edit : USer will always exist because it is logged-in user
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User", userId));

        // Step 2 : Get the blog with given blogId and userId
        Blog blog = blogRepository.findByBlogIdAndAuthor(blogId, user).
                orElseThrow(()-> new ResourceNotFoundException("Blog", blogId, "User", userId));


        // Blog is in persistence context and will get automatically saved because it is dirty now form next line
        blog.setContent(blogCreateRequestDTO.getContent());
        blog.setCategory(blogCreateRequestDTO.getCategory());
        blog.setCanComment(blogCreateRequestDTO.getCanComment());
        blog.setHeading(blogCreateRequestDTO.getHeading());

        // I do not need to save it manually but still i am doing it to make sure that before converting blog ---> BlogResponseDTO. blog gets saved into db
        blog = blogRepository.save(blog);

        BlogCreateResponseDTO response =  modelMapper.map(blog, BlogCreateResponseDTO.class);

        List<CommentCreateResponse>commentCreateResponseDTOList = blog.getComments().stream()
                        .map(comment -> modelMapper.map(comment, CommentCreateResponse.class))
                .toList();

        response.setLikeCount(blog.getLikes() != null ? (long) blog.getLikes().size() : 0L);
        response.setComments(commentCreateResponseDTOList);
        return response;
    }

    @Override
    public BlogListResponse getCategoryBlogs(String categoryName, Integer pageSize, Integer pageNumber, String sortOrder, String sortBy) {
        // Step 1 : Check if this category is valid
        Category enumCategory;
        try {
            enumCategory = Category.valueOf(categoryName.toUpperCase());
        } catch (Exception e) {
            throw new InvalidFileNameException(categoryName, categoryName + " is an invalid Category");
        }

        // Step 2 : Prepare your page
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Blog> page = blogRepository.findByCategory(enumCategory, pageable);
        if(page.getContent().isEmpty()) throw new EmptyResourceException("Blog List for this category");

        // Step 3 ; Prepare your response
        return getBlogListResponse(page);
    }

    @Override
    public BlogListResponse getBlogsByHeading(String heading, Integer pageSize, Integer pageNumber, String sortOrder, String sortBy) {
        // Step 2 : Prepare your page
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Blog> page = blogRepository.findByHeadingContaining(heading, pageable);
        if(page.getContent().isEmpty()) throw new EmptyResourceException("Blog List including this Heading");

        // Step 3 ; Prepare your response
        return getBlogListResponse(page);
    }

    @Override
    public BlogListResponse getAllBlogs(Integer pageSize, Integer pageNumber, String sortOrder, String sortBy) {
        // Step 2 : Prepare your page
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Blog> page = blogRepository.findAll(pageable);
        if(page.getContent().isEmpty()) throw new EmptyResourceException("Blog List");

        // Step 3 ; Prepare your response
        return getBlogListResponse(page);
    }

    @Override
    @Transactional
    public BlogCreateResponseDTO deleteBlogById(Long userId, Long blogId) {
        // Step 1 : Get the blog with given blogId and userId
        Blog blog = blogRepository.findByBlogIdAndAuthor_UserId(blogId, userId).
                orElseThrow(()-> new ResourceNotFoundException("Blog", blogId, "User", userId));

        blogRepository.delete(blog);

        BlogCreateResponseDTO response =  modelMapper.map(blog, BlogCreateResponseDTO.class);
        response.setAuthorId(userId);
        response.setLikeCount(blog.getLikes() != null ? (long)blog.getLikes().size() : 0L);

        return response;
    }

    private BlogListResponse getBlogListResponse(Page<Blog> page) {
        List<BlogCreateResponseDTO> blogCreateResponseDTOList = page.getContent().stream()
                .map(blog -> {
                    BlogCreateResponseDTO blogDto =
                            modelMapper.map(blog, BlogCreateResponseDTO.class);

                    List<CommentCreateResponse> commentDtoList =
                            blog.getComments() == null
                                    ? List.of()
                                    : blog.getComments().stream()
                                    .map(comment ->
                                            modelMapper.map(comment, CommentCreateResponse.class))
                                    .toList();

                    blogDto.setComments(commentDtoList);
                    blogDto.setLikeCount(
                            blog.getLikes() != null ? (long) blog.getLikes().size() : 0
                    );

                    return blogDto;
                })
                .toList();

        return BlogListResponse.builder()
                .content(blogCreateResponseDTOList)
                .pageNumber(page.getNumber())
                .isLastPage(page.isLast())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageSize(page.getSize())
                .build();
    }
}
