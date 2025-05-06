package com.moonjin.realworld.service;

import com.moonjin.realworld.domain.article.Article;
import com.moonjin.realworld.domain.article.Tag;
import com.moonjin.realworld.domain.user.User;
import com.moonjin.realworld.dto.request.ArticleCreate;
import com.moonjin.realworld.dto.request.ArticleEdit;
import com.moonjin.realworld.dto.request.ArticleParam;
import com.moonjin.realworld.dto.response.ArticleListResponse;
import com.moonjin.realworld.dto.response.ArticleResponse;
import com.moonjin.realworld.dto.response.Tags;
import com.moonjin.realworld.repository.ArticleFavoriteRepository;
import com.moonjin.realworld.repository.ArticleRepository;
import com.moonjin.realworld.repository.TagRepository;
import com.moonjin.realworld.common.exception.NotFoundArticleException;
import com.moonjin.realworld.common.exception.Unauthorized;
import com.moonjin.realworld.dto.response.Profile;
import com.moonjin.realworld.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public ArticleResponse create(ArticleCreate request, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(Unauthorized::new);

        List<Tag> tags = resolveTags(request.getTagList());

        Article article = Article.of(request, author);
        article.addTags(tags);

        articleRepository.save(article);

        return new ArticleResponse(article, Profile.of(author, false), false);
    }

    @Transactional
    public ArticleResponse getBySlug(String slug, Long sessionId) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(NotFoundArticleException::new);

        User user = userRepository.findById(article.getAuthor().getId())
                .orElseThrow(Unauthorized::new);
        boolean exists = false;
        if(sessionId != null) {
            exists = articleFavoriteRepository.existsByArticleAndUserId(article, sessionId);
        }

        return new ArticleResponse(article, Profile.of(user, false), exists);
    }

    @Transactional
    public ArticleResponse edit(String slug, ArticleEdit request, Long authorId) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(NotFoundArticleException::new);
        if(article.isNotAuth(authorId)) {
            throw new Unauthorized();
        }

        User user = userRepository.findById(article.getAuthor().getId())
                .orElseThrow(Unauthorized::new);

        article.edit(request);

        return new ArticleResponse(article, Profile.of(user, false), false);
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

        User user = userRepository.findById(userId)
                .orElseThrow(Unauthorized::new);
        article.favoriteBy(user);
        articleRepository.save(article);

        return new ArticleResponse(article, Profile.of(user, true), true);
    }

    @Transactional
    public ArticleResponse unFavorite(String slug, Long userId) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(NotFoundArticleException::new);

        User user = userRepository.findById(userId)
                .orElseThrow(NotFoundArticleException::new);

        articleFavoriteRepository.findByArticleAndUserId(article, userId)
                .ifPresent(articleFavoriteRepository::delete);
        article.unFavoriteBy(user);
        articleRepository.save(article);

        User author = userRepository.findById(article.getAuthor().getId())
                .orElseThrow(Unauthorized::new);
        Profile profile = Profile.of(author, false);

        return new ArticleResponse(article, profile, false);
    }

    @Transactional()
    public ArticleListResponse getList(ArticleParam param, Long userId) {
        return articleRepository.getList(param, userId);
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
