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
    private Long authorId;

    @Column()
    private String slug;

    @Column()
    private String title;

    @Column(length = 50, nullable = false)
    private String description;

    @Column()
    private String body;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleTag> articleTags = new ArrayList<>();

//    @OneToMany( mappedBy = "articles", cascade = CascadeType.ALL)
//    private List<Comment> comments;

    @Builder
    public Article(String body, String title, String description, Long authorId) {
        this.title = title;
        this.slug = makeSlug(title);
        this.description = description;
        this.authorId = authorId;
        this.body = body;
    }

    public static Article of(ArticleCreate request, Long authorId) {
        return Article.builder()
                .authorId(authorId)
                .title(request.getTitle())
                .description(request.getDescription())
                .body(request.getBody())
                .build();
    }

    public void addTags(List<Tag> tags) {
        tags.forEach(tag -> {
            ArticleTag articleTag = new ArticleTag(this, tag);
            articleTags.add(articleTag);
            tag.getArticleTags().add(articleTag);
        });
    }

//    public void addComment(Comment comment) {
//        this.comments.add(comment);
//    }

    public String makeSlug(String title) {
        return title.replaceAll(" ", "-").toLowerCase();
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
