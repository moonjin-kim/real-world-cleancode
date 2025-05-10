package com.moonjin.realworld.repository;

import com.moonjin.realworld.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> ,ArticleRepositoryCustom{
    Optional<Article> findBySlug(String slug);
}
