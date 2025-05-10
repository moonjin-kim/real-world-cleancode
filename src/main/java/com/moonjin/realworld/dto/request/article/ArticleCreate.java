package com.moonjin.realworld.dto.request.article;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ArticleCreate {
    private String title;
    private String description;
    private String body;
    private List<String> tagList;

    @Builder
    public ArticleCreate(String title, String description, String body, List<String> tagList) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.tagList = tagList;
    }
}
