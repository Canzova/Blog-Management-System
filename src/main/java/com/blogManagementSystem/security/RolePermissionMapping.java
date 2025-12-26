package com.blogManagementSystem.security;

import com.blogManagementSystem.dto.constants.PermissionType;
import com.blogManagementSystem.dto.constants.ROLE;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.blogManagementSystem.dto.constants.PermissionType.*;
import static com.blogManagementSystem.dto.constants.ROLE.*;

//@Component
public class RolePermissionMapping {
    public static final Map<ROLE, Set<PermissionType>> map = Map.of(
        USER, Set.of(BLOG_READ,COMMENT_READ, BLOG_EDIT, COMMENT_EDIT, BLOG_WRITE, COMMENT_WRITE, BLOG_LIKE, BLOG_UNLIKE),
            ADMIN, Set.of(BLOG_READ,COMMENT_READ, BLOG_EDIT, COMMENT_EDIT, BLOG_WRITE, COMMENT_WRITE,
                    BLOG_LIKE, BLOG_UNLIKE, USER_DELETE)
    );

    public static Set<SimpleGrantedAuthority>getAuthoritiesForThisRole(ROLE role){
        return map.get(role).stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
