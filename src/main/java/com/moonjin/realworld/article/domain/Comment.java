package com.moonjin.realworld.article.domain;

import com.moonjin.realworld.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User author;

    @Column
    private String title;

    @ManyToOne
    @JoinColumn
    private Article article;

    @Builder
    public Comment(User author, String title, Article article) {
        this.author = author;
        this.title = title;
        this.article = article;
    }

    public static Comment of(Comment comment, User author, Article article) {
        return Comment.builder()
                .author(author)
                .article(article)
                .title(comment.title)
                .build();
    }


}
