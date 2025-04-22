package com.moonjin.realworld.article.service;

import com.moonjin.realworld.article.domain.Article;
import com.moonjin.realworld.article.domain.Tag;
import com.moonjin.realworld.article.dto.request.ArticleCreate;
import com.moonjin.realworld.article.dto.response.ArticleResponse;
import com.moonjin.realworld.article.repository.ArticleRepository;
import com.moonjin.realworld.article.repository.TagRepository;
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
}
