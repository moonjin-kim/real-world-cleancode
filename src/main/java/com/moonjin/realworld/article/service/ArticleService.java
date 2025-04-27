package com.moonjin.realworld.article.service;

import com.moonjin.realworld.article.domain.Article;
import com.moonjin.realworld.article.domain.Tag;
import com.moonjin.realworld.article.dto.request.ArticleCreate;
import com.moonjin.realworld.article.dto.request.ArticleEdit;
import com.moonjin.realworld.article.dto.request.ArticleParam;
import com.moonjin.realworld.article.dto.response.ArticleListResponse;
import com.moonjin.realworld.article.dto.response.ArticleResponse;
import com.moonjin.realworld.article.dto.response.Tags;
import com.moonjin.realworld.article.port.UserPort;
import com.moonjin.realworld.article.repository.ArticleFavoriteRepository;
import com.moonjin.realworld.article.repository.ArticleRepository;
import com.moonjin.realworld.article.repository.TagRepository;
import com.moonjin.realworld.common.exception.NotFoundArticleException;
import com.moonjin.realworld.common.exception.Unauthorized;
import com.moonjin.realworld.user.dto.response.Profile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final ArticleFavoriteRepository articleFavoriteRepository;
    private final UserPort userPort;

    @Transactional
    public ArticleResponse create(ArticleCreate request, Long authorId) {
        Profile profile = userPort.getProfileFrom(authorId, authorId);

        List<Tag> tags = resolveTags(request.getTagList());

        Article article = Article.of(request, authorId);
        article.addTags(tags);

        articleRepository.save(article);

        return new ArticleResponse(article, profile, false);
    }

    @Transactional
    public ArticleResponse getBySlug(String slug, Long sessionId) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(NotFoundArticleException::new);

        Profile profile = userPort.getProfileFrom(article.getAuthorId(), sessionId);
        boolean exists = false;
        if(sessionId != null) {
            exists = articleFavoriteRepository.existsByArticleAndUserId(article, sessionId);
        }

        return new ArticleResponse(article, profile, exists);
    }

    @Transactional
    public ArticleResponse edit(String slug, ArticleEdit request, Long authorId) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(NotFoundArticleException::new);
        if(article.isNotAuth(authorId)) {
            throw new Unauthorized();
        }

        Profile profile = userPort.getProfileFrom(authorId, authorId);

        article.edit(request);

        return new ArticleResponse(article, profile, false);
    }

    @Transactional
    public String delete(String slug, Long authorId) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(NotFoundArticleException::new);
        if(article.isNotAuth(authorId)) {
            throw new Unauthorized();
        }

        articleRepository.delete(article);


        return "Delete " + slug + "Article";
    }

    @Transactional
    public ArticleResponse favorite(String slug, Long userId) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(NotFoundArticleException::new);

        Profile profile = userPort.getProfileFrom(article.getAuthorId(), article.getAuthorId());
        article.favoriteBy(userId);
        articleRepository.save(article);

        return new ArticleResponse(article, profile, true);
    }

    @Transactional
    public ArticleResponse unFavorite(String slug, Long userId) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(NotFoundArticleException::new);

        articleFavoriteRepository.findByArticleAndUserId(article, userId)
                .ifPresent(articleFavoriteRepository::delete);
        article.unFavoriteBy(userId);
        articleRepository.save(article);

        Profile profile = userPort.getProfileFrom(article.getAuthorId(), article.getAuthorId());

        return new ArticleResponse(article, profile, false);
    }

    @Transactional()
    public ArticleListResponse getList(ArticleParam param) {
        return articleRepository.getList(param);
    }

    public Tags getTags() {
        return new Tags(tagRepository.findAll().stream().map(Tag::getName).collect(Collectors.toList()));
    }

    private List<Tag> resolveTags(List<String> tagNames) {
        return tagNames.stream()
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(new Tag(name))))
                .toList();
    }

}
