package com.moonjin.realworld.article.domain;

import com.moonjin.realworld.article.dto.request.ArticleCreate;
import com.moonjin.realworld.article.dto.request.ArticleEdit;
import com.moonjin.realworld.common.domain.DateEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Column(nullable = false)
    private Long likeCount = 0L;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleFavorite> articleFavorites = new ArrayList<>();

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

    public void edit(ArticleEdit request) {
        Optional.ofNullable(request.getTitle())
                .ifPresent(this::putTitle);

        Optional.ofNullable(request.getDescription())
                .ifPresent(this::putDescribe);

        Optional.ofNullable(request.getBody())
                .ifPresent(this::putBody);
    }

    // 좋아요 누르기
    public void favoriteBy(Long userId) {
        boolean already = articleFavorites.stream()
                .anyMatch(l -> l.getUserId().equals(userId));
        if (already) {
            throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
        }
        articleFavorites.add(new ArticleFavorite(userId, this));
    }

    public void unFavoriteBy(Long userId) {
        articleFavorites.removeIf(l -> l.getUserId().equals(userId));
    }

//    public void addComment(Comment comment) {
//        this.comments.add(comment);
//    }

    public String makeSlug(String title) {
        return title.replaceAll(" ", "-").toLowerCase();
    }

    public boolean isNotAuth(Long authorId) {
        return !Objects.equals(this.authorId, authorId);
    }

    public List<String> getTagList() {
        return this.articleTags.stream().map(articleTag -> {
            return articleTag.getTag().getName();
        }).toList();
    }

    public Long getFavoritesCount() {
        return (long) articleFavorites.size();
    }

    private void putTitle(String title) {
        this.title = title;
        this.slug = makeSlug(title);
    }

    private void putDescribe(String description) {
        this.description = description;
    }

    private void putBody(String body) {
        this.body = body;
    }
}
