package com.blogManagementSystem.dto.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionType {

    BLOG_READ("blog:read"),
    COMMENT_READ("comment:read"),
    BLOG_EDIT("blog:edit"),
    COMMENT_EDIT("comment:edit"),
    BLOG_WRITE("blog:write"),
    COMMENT_WRITE("comment:write"),
    BLOG_DELETE("blog:delete"),
    COMMENT_DELETE("comment:delete"),
    BLOG_LIKE("blog:like"),
    BLOG_UNLIKE("blog:unlike"),
    USER_DELETE("user:delete");


    private final String permission;
}
