package com.moonjin.realworld.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moonjin.realworld.domain.article.Article;
import com.moonjin.realworld.dto.request.article.ArticleCreate;
import com.moonjin.realworld.dto.request.article.ArticleEdit;
import com.moonjin.realworld.repository.ArticleFavoriteRepository;
import com.moonjin.realworld.repository.ArticleRepository;
import com.moonjin.realworld.repository.TagRepository;
import com.moonjin.realworld.domain.user.User;
import com.moonjin.realworld.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.moonjin.realworld.controller.UserController.SESSION_USER_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ArticleFavoriteRepository articleFavoriteRepository;

    @AfterEach
    void clean() {
        articleFavoriteRepository.deleteAll();
        articleRepository.deleteAll();
        tagRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("article 작성")
    void createArticleTest() throws Exception {
        // given
        User sessionUser = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("password")
                .build();
        userRepository.save(sessionUser);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_USER_KEY, sessionUser.getId());

        List<String> tags = List.of("Spring", "Java");
        ArticleCreate request = ArticleCreate.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .tagList(tags)
                .build();
        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/api/articles")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("how-to-train-your-dragon"))
                .andExpect(jsonPath("$.title").value("How to train your dragon"))
                .andExpect(jsonPath("$.author.username").value("tester"))
                .andExpect(jsonPath("$.description").value("Ever wonder how?"))
                .andExpect(jsonPath("$.body").value("It takes a Jacobian"));

        // then
        assertEquals(1L, articleRepository.count());
        assertEquals(2L, tagRepository.count());
    }

    @Test
    @DisplayName("article slug로 조회")
    void getArticleAPITest() throws Exception {
        // given
        User sessionUser = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("password")
                .build();
        userRepository.save(sessionUser);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_USER_KEY, sessionUser.getId());

        Article article = articleRepository.save(Article.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .author(sessionUser)
                .build()
        );

        // when
        // then
        mockMvc.perform(get("/api/articles/how-to-train-your-dragon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("how-to-train-your-dragon"))
                .andExpect(jsonPath("$.title").value("How to train your dragon"))
                .andExpect(jsonPath("$.author.username").value("tester"))
                .andExpect(jsonPath("$.description").value("Ever wonder how?"))
                .andExpect(jsonPath("$.body").value("It takes a Jacobian"));
    }

    @Test
    @DisplayName("article slug로 글 수정")
    void putArticleAPITest() throws Exception {
        // given
        User sessionUser = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("password")
                .build();
        userRepository.save(sessionUser);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_USER_KEY, sessionUser.getId());

        Article article = articleRepository.save(Article.builder()
                .title("How to train your dragon")
                .body("It takes a Jacobian")
                .description("Ever wonder how?")
                .author(sessionUser)
                .build()
        );

        ArticleEdit request = ArticleEdit.builder()
                .title("How to make your dragon")
                .body("Maybe Jall?")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // when
        // then
        mockMvc.perform(put("/api/articles/how-to-train-your-dragon").session(session)
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("how-to-make-your-dragon"))
                .andExpect(jsonPath("$.title").value("How to make your dragon"))
                .andExpect(jsonPath("$.author.username").value("tester"))
                .andExpect(jsonPath("$.description").value("Ever wonder how?"))
                .andExpect(jsonPath("$.body").value("Maybe Jall?"));
    }
}
