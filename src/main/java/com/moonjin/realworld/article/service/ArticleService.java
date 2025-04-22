package com.moonjin.realworld.article.service;

import com.moonjin.realworld.article.domain.Article;
import com.moonjin.realworld.article.domain.Tag;
import com.moonjin.realworld.article.dto.request.ArticleCreate;
import com.moonjin.realworld.article.dto.response.ArticleResponse;
import com.moonjin.realworld.article.port.UserPort;
import com.moonjin.realworld.article.repository.ArticleRepository;
import com.moonjin.realworld.article.repository.TagRepository;
import com.moonjin.realworld.common.exception.NotFoundArticleException;
import com.moonjin.realworld.user.dto.response.Profile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final UserPort userPort;

    @Transactional
    public ArticleResponse create(ArticleCreate request, Long authorId) {
        Profile profile = userPort.getProfileFrom(authorId, authorId);

        List<Tag> tags = resolveTags(request.getTagList());

        Article article = Article.of(request, authorId);
        article.addTags(tags);

        articleRepository.save(article);

        return new ArticleResponse(article, profile);
    }

    @Transactional
    public ArticleResponse getBySlug(String slug, Long sessionId) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(NotFoundArticleException::new);

        Profile profile = userPort.getProfileFrom(article.getAuthorId(), sessionId);

        return new ArticleResponse(article, profile);
    }

    public List<Tag> resolveTags(List<String> tagNames) {
        return tagNames.stream()
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(new Tag(name))))
                .toList();
    }

}
