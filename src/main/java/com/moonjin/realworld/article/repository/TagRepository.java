package com.moonjin.realworld.article.repository;

import com.moonjin.realworld.article.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
