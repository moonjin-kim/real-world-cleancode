package com.moonjin.realworld.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArticleParam {
    private Integer limit;
    private Integer offset;
    String tag;
    String author;
    String favorited;

}
