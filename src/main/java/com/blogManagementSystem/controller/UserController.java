package com.blogManagementSystem.controller;
import com.blogManagementSystem.config.AppConstants;
import com.blogManagementSystem.dto.BlogCreateResponseDTO;
import com.blogManagementSystem.dto.BlogListResponse;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/viewmyblogs")
    public ResponseEntity<BlogListResponse> getMyBlogs(
                                                       @RequestParam(name = "pageNo", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNo,
                                                       @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                       @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_BLOGS_BY_Id) String sortBy,
                                                       @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.SORT_DIR) String sortOrder
                                                       ){
        // Logged-in user
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogListResponse blogListResponse = userService.getMyBlogs(loggedInUser.getUserId(), sortBy, sortOrder, pageNo, pageSize);
        return new ResponseEntity<>(blogListResponse, HttpStatus.OK);
    }

    @PatchMapping("/like/{blogId}")
    public ResponseEntity<BlogCreateResponseDTO> likeBlogByBlogId(@PathVariable Long blogId){
        // Logged-in user
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogCreateResponseDTO likedBlogDTO = userService.likeBlogByBlogId(blogId, loggedInUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(likedBlogDTO);
    }

    @DeleteMapping("/unlike/{blogId}")
    public ResponseEntity<BlogCreateResponseDTO> unLikeBlogByBlogId(@PathVariable Long blogId){
        // Logged-in user
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogCreateResponseDTO likedBlogDTO = userService.unLikeBlogByBlogId(blogId, loggedInUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(likedBlogDTO);
    }


}
