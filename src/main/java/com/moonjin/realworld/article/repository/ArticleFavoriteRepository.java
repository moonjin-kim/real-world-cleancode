package com.moonjin.realworld.article.repository;

import com.moonjin.realworld.article.domain.Article;
import com.moonjin.realworld.article.domain.ArticleFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavorite, Long> {
    Optional<ArticleFavorite> findByArticleAndUserId(Article article, Long userId);
    boolean existsByArticleAndUserId(Article article, Long userId);
}
