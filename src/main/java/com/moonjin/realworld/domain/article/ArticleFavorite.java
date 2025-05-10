package com.moonjin.realworld.domain.article;

import com.moonjin.realworld.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(
        name = "favorites",
        uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "article_id" })
)
public class ArticleFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Builder
    public ArticleFavorite(User user, Article article) {
        this.user = user;
        this.article = article;
    }
}
