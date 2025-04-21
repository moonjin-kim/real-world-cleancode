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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Entity
@Table(name = "articles")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Article extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleTag> articleTags = new ArrayList<>();

    @OneToMany( mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

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

    public void addTag(Tag tag) {
        ArticleTag articleTag = new ArticleTag(this, tag);
        articleTags.add(articleTag);
        tag.getArticleTags().add(articleTag);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public String getSlug() {
        return this.title.replaceAll(" ", "-").toLowerCase();
    }

    public List<String> getTagList() {
        return this.articleTags.stream().map(articleTag -> {
            return articleTag.getTag().getName();
        }).toList();
    }

    public Long getFavoritesCount() {
        return 0L;
    }
}
