package com.blogManagementSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "blog_likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "blog_id"})
        }
)
/*
    If we do by-directional mapping and make orphanRemoval true on blog and user, then it won't work,
    because here BlogLike have2 different parents

    @OnDelete(action = OnDeleteAction.CASCADE) will make sure that if the user or blog is deleted then the corresponding
    BlogLike row gets deleted
 */
public class BlogLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogLikeId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name="blog_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Blog blog;
}
