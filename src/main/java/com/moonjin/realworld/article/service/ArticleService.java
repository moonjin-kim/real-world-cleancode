package com.moonjin.realworld.article.service;

import com.moonjin.realworld.article.domain.Article;
import com.moonjin.realworld.article.domain.Tag;
import com.moonjin.realworld.article.dto.request.ArticleCreate;
import com.moonjin.realworld.article.dto.response.ArticleResponse;
import com.moonjin.realworld.article.repository.ArticleRepository;
import com.moonjin.realworld.article.repository.TagRepository;
import com.moonjin.realworld.common.exception.Unauthorized;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Transactional
    ArticleResponse create(ArticleCreate request, Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(Unauthorized::new);

        List<Tag> tags = resolveTags(request.getTagList());

        Article article = Article.of(request, author);
        article.addTags(tags);

        articleRepository.save(article);

        return new ArticleResponse(article);
    }

    public List<Tag> resolveTags(List<String> tagNames) {
        return tagNames.stream()
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(new Tag(name))))
                .toList();
    }

}
