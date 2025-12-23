package com.blogManagementSystem.service;

import com.blogManagementSystem.dto.BlogCreateRequestDTO;
import com.blogManagementSystem.dto.BlogCreateResponseDTO;
import com.blogManagementSystem.entity.Blog;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.exception.ResourceNotFoundException;
import com.blogManagementSystem.repository.BlogRepository;
import com.blogManagementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BlogRepository blogRepository;

    @Override
    @Transactional
    public BlogCreateResponseDTO writeBlog(Long id, BlogCreateRequestDTO blogCreateRequestDTO) {
        // Step 1 : Check if ths user exists in db and get it

        User user = userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("User", id));
        // User is not stored in persistence context

        Blog blog = Blog.builder()
                .content(blogCreateRequestDTO.getContent())
                .category(blogCreateRequestDTO.getCategory())
                .canComment(blogCreateRequestDTO.getCanComment())
                .author(user)
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

        // Step 1 : Check if ths user exists in db and get it
        User user = userRepository.findById(blogId).
                orElseThrow(() -> new ResourceNotFoundException("User", blogId));

        // Step 2 : Get the blog with given blogId and userId
        Blog blog = blogRepository.findByBlogIdAndAuthor(blogId, user).
                orElseThrow(()-> new ResourceNotFoundException("Blog", blogId, "User", userId));


        // Blog is in persistence context and will get automatically saved because it is dirty now form next line
        blog.setContent(blogCreateRequestDTO.getContent());
        blog.setCategory(blogCreateRequestDTO.getCategory());
        blog.setCanComment(blogCreateRequestDTO.getCanComment());

        // I do not need to save it manually but still i am doing it to make sure that before converting blog ---> BlogResponseDTO. blog gets saved into db
        blog = blogRepository.save(blog);

        BlogCreateResponseDTO response =  modelMapper.map(blog, BlogCreateResponseDTO.class);
        response.setLikeCount(blog.getLikes() != null ? (long) blog.getLikes().size() : 0L);
        return response;
    }
}
