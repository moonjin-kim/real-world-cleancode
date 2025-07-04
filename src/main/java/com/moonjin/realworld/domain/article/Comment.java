package com.moonjin.realworld.domain.article;

import com.moonjin.realworld.common.domain.DateEntity;
import com.moonjin.realworld.domain.user.User;
import com.moonjin.realworld.dto.request.article.CommentCreate;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User author;

    @Column
    private String body;

    @ManyToOne
    @JoinColumn
    private Article article;

    @Builder
    public Comment(User author, String body, Article article) {
        this.author = author;
        this.body = body;
        this.article = article;
    }

    public static Comment of(Article article, User author, CommentCreate request) {
        log.info("test");
        return Comment.builder()
                .author(author)
                .article(article)
                .body(request.getBody())
                .build();
    }


}
