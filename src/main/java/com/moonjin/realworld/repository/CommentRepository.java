package com.moonjin.realworld.repository;

import com.moonjin.realworld.domain.article.Article;
import com.moonjin.realworld.domain.article.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
