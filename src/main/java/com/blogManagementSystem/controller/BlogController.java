package com.blogManagementSystem.controller;

import com.blogManagementSystem.config.AppConstants;
import com.blogManagementSystem.dto.BlogCreateRequestDTO;
import com.blogManagementSystem.dto.BlogCreateResponseDTO;
import com.blogManagementSystem.dto.BlogListResponse;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @PostMapping("/write")
    public ResponseEntity<BlogCreateResponseDTO> writeBlog(@Valid @RequestBody BlogCreateRequestDTO blogCreateRequestDTO){
        // Logged-in user
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogCreateResponseDTO blogCreateResponseDTO = blogService.writeBlog(loggedInUser.getUserId(), blogCreateRequestDTO);
        return new ResponseEntity<>(blogCreateResponseDTO, HttpStatus.CREATED);

    }

    @PutMapping("/edit/blog/{blogId}")
    public ResponseEntity<BlogCreateResponseDTO> editBlog(@Valid @RequestBody BlogCreateRequestDTO blogCreateRequestDTO,
                                                          @PathVariable Long blogId){
        // Logged-in user
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogCreateResponseDTO blogCreateResponseDTO = blogService.editBlog(loggedInUser.getUserId(), blogId, blogCreateRequestDTO);
        return new ResponseEntity<>(blogCreateResponseDTO, HttpStatus.CREATED);

    }

    @GetMapping("/category/{category}")
    public ResponseEntity<BlogListResponse> getCategoryBlogs(@PathVariable(name = "category") String categoryName,
                                                             @RequestParam(name="pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                             @RequestParam(name = "pageNo", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                             @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.SORT_DIR) String sortOrder,
                                                             @RequestParam(name = "sortBy", required=false, defaultValue = AppConstants.SORT_BLOGS_BY_Id) String sortBy){
        BlogListResponse categoryBlogResponse = blogService.getCategoryBlogs(categoryName, pageSize, pageNumber, sortOrder, sortBy);
        return new ResponseEntity<>(categoryBlogResponse, HttpStatus.OK);
    }


    @GetMapping("/heading/{heading}")
    public ResponseEntity<BlogListResponse> getBlogsByHeading(@PathVariable(name = "heading") String heading,
                                                             @RequestParam(name="pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                             @RequestParam(name = "pageNo", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                             @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.SORT_DIR) String sortOrder,
                                                             @RequestParam(name = "sortBy", required=false, defaultValue = AppConstants.SORT_BLOGS_BY_Id) String sortBy){
        BlogListResponse categoryBlogResponse = blogService.getBlogsByHeading(heading, pageSize, pageNumber, sortOrder, sortBy);
        return new ResponseEntity<>(categoryBlogResponse, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<BlogListResponse> getAllBlogs(@RequestParam(name="pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                             @RequestParam(name = "pageNo", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                             @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.SORT_DIR) String sortOrder,
                                                             @RequestParam(name = "sortBy", required=false, defaultValue = AppConstants.SORT_BLOGS_BY_Id) String sortBy){
        BlogListResponse categoryBlogResponse = blogService.getAllBlogs(pageSize, pageNumber, sortOrder, sortBy);
        return new ResponseEntity<>(categoryBlogResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{blogId}")
    public ResponseEntity<BlogCreateResponseDTO> deleteBlogById(@PathVariable Long blogId){
        // Logged-in user
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogCreateResponseDTO deletedBlog = blogService.deleteBlogById(loggedInUser.getUserId(), blogId);
        return new ResponseEntity<>(deletedBlog, HttpStatus.OK);
    }
}
