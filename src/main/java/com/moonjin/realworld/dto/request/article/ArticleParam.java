package com.moonjin.realworld.dto.request.article;

import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.min;

@Getter
@Builder
public class ArticleParam {
    private static final int MAX_PAGE = 999;
    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer limit = 10;
    @Builder.Default
    private Integer offset = 1;
    String tag;
    String author;
    String favorited;

    public long getPageNum() {
        return (long) (limit - 1) * min(offset, MAX_SIZE);
    }
}
