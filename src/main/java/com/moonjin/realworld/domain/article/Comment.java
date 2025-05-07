package com.moonjin.realworld.domain.article;

import com.moonjin.realworld.common.domain.DateEntity;
import com.moonjin.realworld.domain.user.User;
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
@Table(name = "comments")
public class Comment extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User author;

    @Column
    private String body;

    @ManyToOne
    @JoinColumn
    private Article article;

    @Builder
    public Comment(User author, String title, Article article) {
        this.author = author;
        this.body = title;
        this.article = article;
    }

    public static Comment of(Comment comment, User author, Article article) {
        return Comment.builder()
                .author(author)
                .article(article)
                .title(comment.body)
                .build();
    }


}
