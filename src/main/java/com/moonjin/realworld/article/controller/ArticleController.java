package com.moonjin.realworld.article.controller;

import com.moonjin.realworld.article.dto.request.ArticleCreate;
import com.moonjin.realworld.article.dto.request.ArticleEdit;
import com.moonjin.realworld.article.dto.request.ArticleParam;
import com.moonjin.realworld.article.dto.response.ArticleListResponse;
import com.moonjin.realworld.article.dto.response.ArticleResponse;
import com.moonjin.realworld.article.dto.response.Tags;
import com.moonjin.realworld.article.service.ArticleService;
import com.moonjin.realworld.user.controller.UserController;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/articles")
    public ArticleResponse create(HttpSession httpSession, @RequestBody @Valid ArticleCreate request) {
        Long userId = (Long) httpSession.getAttribute(UserController.SESSION_USER_KEY);

        return articleService.create(request, userId);
    }

    @GetMapping("/articles/{slug}")
    public ArticleResponse getBySlug(HttpSession httpSession, @PathVariable String slug) {
        Long userId = (Long) httpSession.getAttribute(UserController.SESSION_USER_KEY);

        return articleService.getBySlug(slug, userId);
    }

    @PutMapping("/articles/{slug}")
    public ArticleResponse getBySlug(
            HttpSession httpSession,
            @PathVariable String slug,
            @RequestBody @Valid ArticleEdit request
    ) {
        Long userId = (Long) httpSession.getAttribute(UserController.SESSION_USER_KEY);

        return articleService.edit(slug, request, userId);
    }

    @DeleteMapping("/articles/{slug}")
    public String delete(
            HttpSession httpSession,
            @PathVariable String slug
    ) {
        Long userId = (Long) httpSession.getAttribute(UserController.SESSION_USER_KEY);

        return articleService.delete(slug, userId);
    }

    @PostMapping("/articles/{slug}/favorite")
    public ArticleResponse favorite(
            HttpSession httpSession,
            @PathVariable String slug
    ) {
        Long userId = (Long) httpSession.getAttribute(UserController.SESSION_USER_KEY);

        return articleService.favorite(slug, userId);
    }

    @DeleteMapping("/articles/{slug}/favorite")
    public ArticleResponse unFavorite(
            HttpSession httpSession,
            @PathVariable String slug
    ) {
        Long userId = (Long) httpSession.getAttribute(UserController.SESSION_USER_KEY);

        return articleService.unFavorite(slug, userId);
    }

    @GetMapping("/articles")
    public ArticleListResponse getList(
            HttpSession httpSession,
            @ModelAttribute ArticleParam articleParam
    ) {
        Long userId = (Long) httpSession.getAttribute(UserController.SESSION_USER_KEY);

        return articleService.getList(articleParam);
    }

    @GetMapping("/tags")
    public Tags getTags() {
        return articleService.getTags();
    }
}
