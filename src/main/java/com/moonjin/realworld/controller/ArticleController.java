package com.moonjin.realworld.controller;

import com.moonjin.realworld.dto.request.article.ArticleCreate;
import com.moonjin.realworld.dto.request.article.ArticleEdit;
import com.moonjin.realworld.dto.request.article.ArticleParam;
import com.moonjin.realworld.dto.response.article.ArticleListResponse;
import com.moonjin.realworld.dto.response.article.ArticleResponse;
import com.moonjin.realworld.dto.response.article.Tags;
import com.moonjin.realworld.service.ArticleService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

        log.info("getBySlug {}", slug);
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

        return articleService.getList(articleParam, userId);
    }

    @GetMapping("/tags")
    public Tags getTags() {
        return articleService.getTags();
    }
}
