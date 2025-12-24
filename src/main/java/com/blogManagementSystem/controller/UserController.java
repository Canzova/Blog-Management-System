package com.blogManagementSystem.controller;
import com.blogManagementSystem.config.AppConstants;
import com.blogManagementSystem.dto.BlogCreateResponseDTO;
import com.blogManagementSystem.dto.BlogListResponse;
import com.blogManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/viewmyblogs/{userId}")
    public ResponseEntity<BlogListResponse> getMyBlogs(@PathVariable Long userId,
                                                       @RequestParam(name = "pageNo", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNo,
                                                       @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                       @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_BLOGS_BY_Id) String sortBy,
                                                       @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.SORT_DIR) String sortOrder
                                                       ){
        BlogListResponse blogListResponse = userService.getMyBlogs(userId, sortBy, sortOrder, pageNo, pageSize);
        return new ResponseEntity<>(blogListResponse, HttpStatus.OK);
    }

    @PatchMapping("/like/{blogId}/{userId}")
    public ResponseEntity<BlogCreateResponseDTO> likeBlogByBlogId(@PathVariable Long blogId,
                                                                  @PathVariable Long userId){
        BlogCreateResponseDTO likedBlogDTO = userService.likeBlogByBlogId(blogId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(likedBlogDTO);
    }

    @DeleteMapping("/unlike/{blogId}/{userId}")
    public ResponseEntity<BlogCreateResponseDTO> unLikeBlogByBlogId(@PathVariable Long blogId,
                                                                  @PathVariable Long userId){
        BlogCreateResponseDTO likedBlogDTO = userService.unLikeBlogByBlogId(blogId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(likedBlogDTO);
    }


}
