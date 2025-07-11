package com.moonjin.realworld.repository;

import com.moonjin.realworld.dto.request.Page;
import com.moonjin.realworld.dto.request.article.ArticleParam;
import com.moonjin.realworld.dto.response.article.ArticleListResponse;


public interface ArticleRepositoryCustom {
    ArticleListResponse getList(ArticleParam param, Long userId);

    ArticleListResponse getFeed(Page param, Long userId);
}
