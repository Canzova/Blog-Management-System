package com.blogManagementSystem.entity;

import com.blogManagementSystem.dto.constants.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogId;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Blog cannot be blank or null")
    @Size(min=10, max=1000, message = "Blog should be at-least 10 characters long and at-max 1000 characters long.")
    private String content;

    // This has to be an enum
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @CreationTimestamp
    private LocalDateTime publishDateTime;

    private Boolean canComment;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="author_id")
    private User author;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "blog_likes",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User>likes = new HashSet<>();
}
