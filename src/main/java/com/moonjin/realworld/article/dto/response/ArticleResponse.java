package com.moonjin.realworld.article.dto.response;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.moonjin.realworld.article.domain.Article;
import com.moonjin.realworld.article.domain.Tag;
import com.moonjin.realworld.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeName("profile")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Getter
public class ArticleResponse {
    String slug;
    String title;
    String description;
    String body;
    List<Tag> tagList;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean favorited;
    Long favoritesCount;
    Author author;

    @Builder
    public ArticleResponse(Article article) {
        this.slug = article.getSlug();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.body = article.getBody();
        this.tagList = article.getTag();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.favoritesCount = article.getFavoritesCount();
        this.author = new Author(article.getAuthor(), false);
    }
}
