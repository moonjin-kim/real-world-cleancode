package com.moonjin.realworld.article.domain;

import com.moonjin.realworld.article.dto.request.ArticleCreate;
import com.moonjin.realworld.common.domain.DateEntity;
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
@Table(name = "article")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Article extends DateEntity {
    @Id
    private Long id;

    @Column()
    private String title;

    @Column(length = 50, nullable = false)
    private String description;

    @Column()
    private String body;

    @ManyToOne
    @JoinColumn
    private User author;

    @Builder
    public Article(String body, String title, String description, User author) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.body = body;
    }

    public static Article of(ArticleCreate request, User author) {
        return Article.builder()
                .author(author)
                .title(request.getTitle())
                .description(request.getDescription())
                .body(request.getBody())
                .build();
    }
}
