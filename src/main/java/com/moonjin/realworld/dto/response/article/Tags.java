package com.moonjin.realworld.dto.response.article;
import lombok.Getter;

import java.util.List;

@Getter
public class Tags {
    List<String> tags;

    public Tags(List<String> tags) {
        this.tags = tags;
    }
}
