package com.blogManagementSystem.entity;

import com.blogManagementSystem.dto.constants.AuthProviderType;
import com.blogManagementSystem.dto.constants.ROLE;
import com.blogManagementSystem.security.RolePermissionMapping;
import com.blogManagementSystem.service.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name="app_user")
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    String username;

    String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false) // DB level Protection
    private String lastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<ROLE> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private AuthProviderType authProviderType;

    private String providerId;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JsonIgnore
    private List<Blog>blog = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();

       roles.forEach(role->{
           Set<SimpleGrantedAuthority> authorities = RolePermissionMapping.getAuthoritiesForThisRole(role);
           grantedAuthorities.addAll(authorities); // Authorities
           grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.name())); // Role
       });

       return grantedAuthorities;

//       return roles.stream()
//               .map(role1 -> new SimpleGrantedAuthority("ROLE_" + role1.name()))
//               .toList();
    }

}
