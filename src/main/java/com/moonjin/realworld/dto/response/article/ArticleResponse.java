package com.moonjin.realworld.dto.response.article;

import com.moonjin.realworld.domain.article.Article;
import com.moonjin.realworld.dto.response.user.Profile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ArticleResponse {
    String slug;
    String title;
    String description;
    String body;
    List<String> tagList;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean favorited;
    Long favoritesCount;
    Profile author;

    public ArticleResponse() {
    }

    @Builder
    public ArticleResponse(
            String slug,
            String title,
            String description,
            String body,
            List<String> tagList,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            boolean favorited,
            Long favoritesCount,
            Profile author
    ) {
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.body = body;
        this.tagList = tagList;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.favorited = favorited;
        this.favoritesCount = favoritesCount;
        this.author = author;
    }

    public ArticleResponse(Article article, Profile profile, boolean isFavorited) {
        this.slug = article.getSlug();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.body = article.getBody();
        this.tagList = article.getTagList();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.favorited = isFavorited;
        this.favoritesCount = article.getFavoritesCount();
        this.author = profile;
    }
}
