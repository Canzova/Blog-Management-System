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
import org.hibernate.engine.internal.Cascade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private String content;

    // This has to be an enum
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String heading;

    @CreationTimestamp
    private LocalDateTime publishDateTime;

    private Boolean canComment;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="author_id")
    private User author;

    // On Many side we have dy default fetch type as lazy so we can't access it outside a transactional session
    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "blog_likes",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User>likes = new HashSet<>();

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}
