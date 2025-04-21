package com.moonjin.realworld.article.repository;

import com.moonjin.realworld.article.domain.Article;
import com.moonjin.realworld.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
