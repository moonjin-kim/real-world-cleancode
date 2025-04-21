package com.moonjin.realworld.article.domain;

import com.moonjin.realworld.article.dto.request.ArticleCreate;
import com.moonjin.realworld.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    @Test
    @DisplayName("태그를 생성한다")
    void ofTest() {
        // given
        String name = "Spring";

        // when
        Tag tag = Tag.of(name);

        // then
        assertEquals(name, tag.getName());
    }
}
