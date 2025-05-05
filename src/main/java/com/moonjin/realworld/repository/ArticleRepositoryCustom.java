package com.moonjin.realworld.repository;

import com.moonjin.realworld.dto.request.ArticleParam;
import com.moonjin.realworld.dto.response.ArticleListResponse;


public interface ArticleRepositoryCustom {
    ArticleListResponse getList(ArticleParam param, Long userId);
}
