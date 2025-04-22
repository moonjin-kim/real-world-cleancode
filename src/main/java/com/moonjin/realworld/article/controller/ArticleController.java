package com.moonjin.realworld.article.controller;

import com.moonjin.realworld.article.dto.request.ArticleCreate;
import com.moonjin.realworld.article.dto.response.ArticleResponse;
import com.moonjin.realworld.article.service.ArticleService;
import com.moonjin.realworld.user.controller.UserController;
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

        return articleService.getBySlug(slug, userId);
    }
}
