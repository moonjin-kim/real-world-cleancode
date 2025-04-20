package com.moonjin.realworld.article.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ArticleCreate {
    private String title;
    private String description;
    private String body;
    private List<String> tagList;
}
