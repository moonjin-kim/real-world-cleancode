package com.moonjin.realworld.dto.response.article;

import lombok.Getter;

import java.util.List;

@Getter
public class ArticleListResponse {
    List<ArticleResponse> articles;
    Long articlesCount;

    public ArticleListResponse(List<ArticleResponse> articles, Long articlesCount) {
        this.articles = articles;
        this.articlesCount = articlesCount;
    }
}
