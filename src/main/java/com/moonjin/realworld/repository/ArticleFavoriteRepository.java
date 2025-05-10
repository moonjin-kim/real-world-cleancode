package com.moonjin.realworld.repository;

import com.moonjin.realworld.domain.article.Article;
import com.moonjin.realworld.domain.article.ArticleFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavorite, Long> {
    Optional<ArticleFavorite> findByArticleAndUserId(Article article, Long userId);
    boolean existsByArticleAndUserId(Article article, Long userId);
}
