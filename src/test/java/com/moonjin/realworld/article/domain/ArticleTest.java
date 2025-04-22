package com.moonjin.realworld.article.domain;

import com.moonjin.realworld.article.dto.request.ArticleCreate;
import com.moonjin.realworld.article.dto.request.ArticleEdit;
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
        Article result = Article.of(request, 1L);

        // then
        Assertions.assertEquals("How to train your dragon", result.getTitle());
        Assertions.assertEquals("It takes a Jacobian", result.getBody());
        Assertions.assertEquals("Ever wonder how?", result.getDescription());

        Assertions.assertEquals(1L, result.getAuthorId());
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
        Article article = Article.of(request, 1L);

        // when
        String result = article.getSlug();


        // then
        Assertions.assertEquals("how-to-train-your-dragon", result);
    }

    @Test
    @DisplayName("article을 수정한다.")
    void editTest() {
        // given
        User author = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        Article article = Article.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .authorId(author.getId())
                .build();

        ArticleEdit articleEdit = ArticleEdit.builder()
                .title("How to train your dragonBall")
                .body("It takes a ball")
                .description("why wonder how?")
                .build();

        // when
        article.edit(articleEdit);


        // then
        Assertions.assertEquals("how-to-train-your-dragonball", article.getSlug());
        Assertions.assertEquals("How to train your dragonBall", article.getTitle());
        Assertions.assertEquals("It takes a ball", article.getBody());
        Assertions.assertEquals("why wonder how?", article.getDescription());
    }

    @Test
    @DisplayName("title을 변경하면 slug도 수정된다.")
    void editSlugTest() {
        // given
        User author = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        Article article = Article.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .authorId(author.getId())
                .build();

        ArticleEdit articleEdit = ArticleEdit.builder()
                .title("How to train your dragonBall")
                .build();

        // when
        article.edit(articleEdit);


        // then
        Assertions.assertEquals("how-to-train-your-dragonball", article.getSlug());
        Assertions.assertEquals("How to train your dragonBall", article.getTitle());
    }

    @Test
    @DisplayName("article을 수정할 때 변경 요청한 값 만 수정된다.")
    void editTest2() {
        // given
        User author = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        Article article = Article.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .authorId(author.getId())
                .build();

        ArticleEdit articleEdit = ArticleEdit.builder()
                .body("It takes a ball")
                .description("why wonder how?")
                .build();

        // when
        article.edit(articleEdit);


        // then
        Assertions.assertEquals("how-to-train-your-dragon", article.getSlug());
        Assertions.assertEquals("How to train your dragon", article.getTitle());
        Assertions.assertEquals("It takes a ball", article.getBody());
        Assertions.assertEquals("why wonder how?", article.getDescription());
    }
}
