package com.blogManagementSystem.entity;

import com.blogManagementSystem.dto.constants.AuthProviderType;
import com.blogManagementSystem.dto.constants.ROLE;
import com.blogManagementSystem.security.RolePermissionMapping;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

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
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
                    flags = Pattern.Flag.CASE_INSENSITIVE)
    String userEmail;

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

    private Boolean verified;

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

    @Override
    public String getUsername() {
        return this.userEmail;
    }

    public void setUsername(String username){
        this.userEmail = username;
    }

}
