package com.moonjin.realworld.dto.request.article;

import com.moonjin.realworld.dto.request.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static java.lang.Math.min;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleParam extends Page {
    String tag;
    String author;
    String favorited;
}
