package com.moonjin.realworld.article.service;

import com.moonjin.realworld.domain.article.Article;
import com.moonjin.realworld.domain.article.ArticleFavorite;
import com.moonjin.realworld.domain.article.Tag;
import com.moonjin.realworld.dto.request.article.ArticleCreate;
import com.moonjin.realworld.dto.request.article.ArticleEdit;
import com.moonjin.realworld.dto.response.article.ArticleResponse;
import com.moonjin.realworld.dto.response.article.Tags;
import com.moonjin.realworld.repository.ArticleFavoriteRepository;
import com.moonjin.realworld.repository.ArticleRepository;
import com.moonjin.realworld.repository.TagRepository;
import com.moonjin.realworld.common.exception.Unauthorized;
import com.moonjin.realworld.service.ArticleService;
import com.moonjin.realworld.domain.user.User;
import com.moonjin.realworld.repository.UserRepository;
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
    private ArticleFavoriteRepository articleFavoriteRepository;
    @Autowired
    private ArticleService articleService;

    @AfterEach
    void clean() {
        articleFavoriteRepository.deleteAll();
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
                .author(user)
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
                .author(user)
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
                .author(user)
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
                .author(user)
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
                .author(user)
                .build()
        );


        // when
        // then
        assertThrows(Unauthorized.class, () -> articleService.delete(
                article.getSlug(), user.getId() + 2));
    }

    @Test
    @DisplayName("유저가 article을 좋아요한다.")
    void favoriteTest1() {
        // given
        User user1 = userRepository.save(User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build());

        User user2 = userRepository.save(User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build());

        Article article = articleRepository.save(Article.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .author(user1)
                .build()
        );


        // when
        ArticleResponse result = this.articleService.favorite(article.getSlug(), user2.getId());

        // then
        assertEquals(true, result.isFavorited());

        List<ArticleFavorite> articleFavorites = articleFavoriteRepository.findAll();
        assertEquals(1, articleFavorites.size());
        assertEquals(user2, articleFavorites.get(0).getUser());
    }

    @Test
    @DisplayName("이미 해당 article에 좋아요 한 유저는 좋아요 할 수 없다.")
    void favoriteTest2() {
        // given
        User user1 = userRepository.save(User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build());

        User user2 = userRepository.save(User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build());

        Article article = articleRepository.save(Article.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .author(user1)
                .build()
        );
        this.articleService.favorite(article.getSlug(), user2.getId());


        // when

        // then
        assertThrows(IllegalStateException.class, () -> this.articleService.favorite(article.getSlug(), user2.getId()));
    }

    @Test
    @DisplayName("유저가 article을 좋아요한 것을 취소한다.")
    void unFavoriteTest1() {
        // given
        User user1 = userRepository.save(User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build());

        User user2 = userRepository.save(User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build());

        Article article = articleRepository.save(Article.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .author(user1)
                .build()
        );
        this.articleService.favorite(article.getSlug(), user2.getId());


        // when
        ArticleResponse result = this.articleService.unFavorite(article.getSlug(), user2.getId());

        // then
        assertEquals(false, result.isFavorited());

        List<ArticleFavorite> articleFavorites = articleFavoriteRepository.findAll();
        assertEquals(0, articleFavorites.size());
    }

//    @Test
//    @DisplayName("이미 해당 article에 좋아요한 적 없으면 취소할 수 없다.")
//    void unFavoriteTest2() {
//        // given
//        User user1 = userRepository.save(User.builder()
//                .email("realword@gmail.com")
//                .password("realworld123!")
//                .username("RealWorld")
//                .build());
//
//        User user2 = userRepository.save(User.builder()
//                .email("realword@gmail.com")
//                .password("realworld123!")
//                .username("RealWorld")
//                .build());
//
//        Article article = articleRepository.save(Article.builder()
//                .title("How to train your dragon")
//                .body("It takes a Jacobian")
//                .description("Ever wonder how?")
//                .authorId(user1.getId())
//                .build()
//        );
//
//
//        // when
//        // then
//        assertThrows(IllegalStateException.class, () -> this.articleService.unFavorite(article.getSlug(), user2.getId()));
//    }

    @Test
    @DisplayName("유저가 article을 좋아요한 것을 취소한다.")
    void getTagTest1() {
        // given
        Tag tag1 = tagRepository.save(new Tag("java"));
        Tag tag2 = tagRepository.save(new Tag("C++"));
        Tag tag3 = tagRepository.save(new Tag("typescript"));


        // when
        Tags result = this.articleService.getTags();

        // then
        assertEquals(3, result.getTags().size());
    }
}
