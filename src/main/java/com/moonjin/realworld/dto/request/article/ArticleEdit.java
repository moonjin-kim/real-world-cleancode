package com.moonjin.realworld.dto.request.article;

import lombok.Builder;
import lombok.Data;

@Data
public class ArticleEdit {
    String title;
    String description;
    String body;

    @Builder
    public ArticleEdit(String title, String description, String body) {
        this.title = title;
        this.description = description;
        this.body = body;
    }
}
