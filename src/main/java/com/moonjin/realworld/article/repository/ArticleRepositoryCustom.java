package com.moonjin.realworld.article.repository;

import com.moonjin.realworld.article.dto.request.ArticleParam;
import com.moonjin.realworld.article.dto.response.ArticleListResponse;


public interface ArticleRepositoryCustom {
    ArticleListResponse getList(ArticleParam param);
}
