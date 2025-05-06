package com.moonjin.realworld.repository;

import com.moonjin.realworld.dto.request.ArticleParam;
import com.moonjin.realworld.dto.response.ArticleListResponse;
import com.moonjin.realworld.dto.response.ArticleResponse;
import com.moonjin.realworld.dto.response.Profile;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.moonjin.realworld.domain.article.QArticle.article;
import static com.moonjin.realworld.domain.article.QArticleFavorite.articleFavorite;
import static com.moonjin.realworld.domain.article.QArticleTag.articleTag;
import static com.moonjin.realworld.domain.article.QTag.tag;
import static com.moonjin.realworld.domain.user.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    @Override
    public ArticleListResponse getList(ArticleParam param, Long userId) {
        long total = queryFactory
                .select(article.count())
                .from(article)
                .where(eqAuthor(param.getAuthor()), eqTag(param.getTag()), eqFavorited(param.getFavorited()))
                .fetchOne();

        List<ArticleResponse> content = queryFactory
                .from(article)
                .leftJoin(user).on(article.author.eq(user))
                .leftJoin(article.articleTags, articleTag)
                .leftJoin(tag).on(articleTag.tag.eq(tag))
                .leftJoin(article.articleFavorites, articleFavorite)
                .where(eqAuthor(param.getAuthor()), eqTag(param.getTag()), eqFavorited(param.getFavorited()))
                .limit(param.getLimit())
                .offset(param.getOffset() * 10)
                .transform(
                        groupBy(article.id).list(
                                Projections.constructor(ArticleResponse.class,
                                        article.slug,
                                        article.title,
                                        article.description,
                                        article.body,
                                        list(tag.name),                   // <— tagList
                                        article._super.createdAt,
                                        article._super.updatedAt,
                                        getFavoritedExpression(userId),      // favorited
                                        article.articleFavorites.size().longValue(),  // favoritesCount
                                        Projections.constructor(Profile.class,
                                                user.username,
                                                user.bio,
                                                user.image,
                                                Expressions.constant(false)     // following
                                        )
                                )
                        )
                );
        return new ArticleListResponse(content, total);
    }

    private Expression<Boolean> getFavoritedExpression(Long userId) {
        if (userId == null) {
            return Expressions.constant(false);
        }
        return Expressions.cases()
                .when(articleFavorite.user.id.eq(userId))
                .then(true)
                .otherwise(false);
    }

    private BooleanExpression eqAuthor(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return article.author.eq(
                queryFactory
                        .select(user)
                        .from(user)
                        .where(user.username.eq(name))
                        .fetchOne()
        );
    }

    private BooleanExpression eqFavorited(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }

        // 1) username → id
        NumberExpression<Long> favUserId = (NumberExpression<Long>) JPAExpressions
                .select(user.id)
                .from(user)
                .where(user.username.eq(username));

        // 2) ArticleFavorite.userId 와 매칭
        return article.articleFavorites
                .any()
                .user.id.eq(favUserId);
    }

    private BooleanExpression eqTag(String tag) {
        if( StringUtils.isEmpty(tag) ) {
            return null;
        }

        return article.articleTags.any().tag.name.eq(tag);
    }
}
