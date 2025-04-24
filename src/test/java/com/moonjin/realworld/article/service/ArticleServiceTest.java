package com.moonjin.realworld.article.service;

import com.moonjin.realworld.article.domain.Article;
import com.moonjin.realworld.article.domain.Tag;
import com.moonjin.realworld.article.dto.request.ArticleCreate;
import com.moonjin.realworld.article.dto.request.ArticleEdit;
import com.moonjin.realworld.article.dto.response.ArticleResponse;
import com.moonjin.realworld.article.repository.ArticleRepository;
import com.moonjin.realworld.article.repository.TagRepository;
import com.moonjin.realworld.common.exception.Unauthorized;
import com.moonjin.realworld.common.exception.UserNotFoundException;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.user.dto.request.Signup;
import com.moonjin.realworld.user.repository.UserRepository;
import com.moonjin.realworld.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ArticleService articleService;

    @AfterEach
    void clean() {
        articleRepository.deleteAll();
        tagRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("새 Article을 생성한다")
    void createTest() {
        // given
        User user = userRepository.save(User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build());

        List<String> tags = List.of("Spring", "Java");
        ArticleCreate request = ArticleCreate.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .tagList(tags)
                .build();

        // when
        ArticleResponse response = articleService.create(request, user.getId());


        // then
        Assertions.assertEquals("how-to-train-your-dragon", response.getSlug());
        Assertions.assertEquals("How to train your dragon", response.getTitle());
        Assertions.assertEquals("It takes a Jacobian", response.getBody());
        Assertions.assertEquals("Ever wonder how?", response.getDescription());

        List<Tag> tag = tagRepository.findAll();
        assertEquals(2, tag.size());
    }

    @Test
    @DisplayName("slug로 article를 조회한다")
    void getBySlugTest() {
        // given
        User user = userRepository.save(User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build());

        Article article = articleRepository.save(Article.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .authorId(user.getId())
                .build()
        );


        // when
        ArticleResponse response = articleService.getBySlug(article.getSlug(), user.getId());


        // then
        Assertions.assertEquals("how-to-train-your-dragon", response.getSlug());
        Assertions.assertEquals("How to train your dragon", response.getTitle());
        Assertions.assertEquals("It takes a Jacobian", response.getBody());
        Assertions.assertEquals("Ever wonder how?", response.getDescription());
    }

    @Test
    @DisplayName("article을 수정한다.")
    void editTest() {
        // given
        User user = userRepository.save(User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build());

        Article article = articleRepository.save(Article.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .authorId(user.getId())
                .build()
        );

        ArticleEdit articleEdit = ArticleEdit.builder()
                .title("How to train your dragonBall")
                .body("It takes a ball")
                .description("why wonder how?")
                .build();


        // when
        ArticleResponse response = articleService.edit(article.getSlug(), articleEdit, user.getId());


        // then
        Assertions.assertEquals("how-to-train-your-dragonball", response.getSlug());
        Assertions.assertEquals("How to train your dragonBall", response.getTitle());
        Assertions.assertEquals("It takes a ball", response.getBody());
        Assertions.assertEquals("why wonder how?", response.getDescription());

        Article editedArticle = articleRepository.findById(article.getId()).get();
        Assertions.assertEquals("how-to-train-your-dragonball", editedArticle.getSlug());
        Assertions.assertEquals("How to train your dragonBall", editedArticle.getTitle());
        Assertions.assertEquals("It takes a ball", editedArticle.getBody());
        Assertions.assertEquals("why wonder how?", editedArticle.getDescription());
    }

    @Test
    @DisplayName("작성자가 아니면 article을 수정할 수 없다")
    void editTest2() {
        // given
        User user = userRepository.save(User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build());

        Article article = articleRepository.save(Article.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .authorId(user.getId())
                .build()
        );

        ArticleEdit articleEdit = ArticleEdit.builder()
                .title("How to train your dragonBall")
                .body("It takes a ball")
                .description("why wonder how?")
                .build();


        // when
        // then
        assertThrows(Unauthorized.class, () -> articleService.edit(
                article.getSlug(),articleEdit, user.getId() + 1));
    }

    @Test
    @DisplayName("작성자는 article을 삭제한다.")
    void deleteTest() {
        // given
        User user = userRepository.save(User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build());

        Article article = articleRepository.save(Article.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .authorId(user.getId())
                .build()
        );


        // when
        articleService.delete(article.getSlug(), user.getId());
        // then
        List<Article> articles = articleRepository.findAll();
        assertEquals(0, articles.size());
    }

    @Test
    @DisplayName("작성자가 아니면 article을 삭제할 수 없다")
    void deleteTest2() {
        // given
        User user = userRepository.save(User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build());

        Article article = articleRepository.save(Article.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .authorId(user.getId())
                .build()
        );


        // when
        // then
        assertThrows(Unauthorized.class, () -> articleService.delete(
                article.getSlug(), user.getId() + 1));
    }
}
