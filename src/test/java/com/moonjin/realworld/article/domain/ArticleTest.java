package com.moonjin.realworld.article.domain;

import com.moonjin.realworld.article.dto.request.ArticleCreate;
import com.moonjin.realworld.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class ArticleTest {

    @Test
    @DisplayName("새로운 기사를 작성한다.")
    void ofTest() {
        // given
        User author = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        ArticleCreate request = ArticleCreate.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .build();

        Tag tag = Tag.of("Spring");

        // when
        Article result = Article.of(request, author);

        // then
        Assertions.assertEquals("How to train your dragon", result.getTitle());
        Assertions.assertEquals("It takes a Jacobian", result.getBody());
        Assertions.assertEquals("Ever wonder how?", result.getDescription());

        Assertions.assertEquals(author, result.getAuthor());
    }


    @Test
    @DisplayName("slug를 생성한다.")
    void getSlugTest() {
        // given
        User author = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        ArticleCreate request = ArticleCreate.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .build();

        Tag tag = Tag.of("Spring");
        Article article = Article.of(request, author);

        // when
        String result = article.getSlug();


        // then
        Assertions.assertEquals("how-to-train-your-dragon", result);
    }
}
