package com.moonjin.realworld.repository;

import com.moonjin.realworld.dto.request.article.ArticleParam;
import com.moonjin.realworld.dto.response.article.ArticleListResponse;
import com.moonjin.realworld.dto.response.article.ArticleResponse;
import com.moonjin.realworld.dto.response.user.Profile;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.moonjin.realworld.domain.article.QArticle.article;
import static com.moonjin.realworld.domain.article.QArticleFavorite.articleFavorite;
import static com.moonjin.realworld.domain.article.QArticleTag.articleTag;
import static com.moonjin.realworld.domain.article.QTag.tag;
import static com.moonjin.realworld.domain.user.QFollow.follow;
import static com.moonjin.realworld.domain.user.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;
import static com.querydsl.core.types.dsl.Expressions.FALSE;

@Slf4j
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public ArticleListResponse getList(ArticleParam param, Long userId) {

        List<ArticleResponse> content = queryFactory
                .from(article)
                .leftJoin(user).on(article.author.eq(user))
                .leftJoin(article.articleTags, articleTag)
                .leftJoin(tag).on(articleTag.tag.eq(tag))
                .leftJoin(article.articleFavorites, articleFavorite)
                .where(eqAuthor(param.getAuthor()), eqTag(param.getTag()), eqFavorited(param.getFavorited()))
                .limit(param.getLimit())
                .offset(param.getPageNum())
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
                                        getFavoritedExpression(userId),
                                        article.articleFavorites.size().longValue(),
                                        Projections.constructor(Profile.class,
                                                user.username,
                                                user.bio,
                                                user.image,
                                                getFollowingExpression(userId)     // following
                                        )
                                )
                        )
                );

        long count = queryFactory
                .select(article.countDistinct().coalesce(0L))
                .from(article)
                .leftJoin(user).on(article.author.eq(user))
                .leftJoin(article.articleTags, articleTag)
                .leftJoin(tag).on(articleTag.tag.eq(tag))
                .leftJoin(article.articleFavorites, articleFavorite)
                .where(
                        eqAuthor(param.getAuthor()),
                        eqTag(param.getTag()),
                        eqFavorited(param.getFavorited())
                )
                .fetchOne();

        return new ArticleListResponse(content, count);
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
        return article.articleFavorites
                .any()
                .user
                .username
                .eq(username);
    }

    private BooleanExpression eqTag(String tag) {
        if( StringUtils.isEmpty(tag) ) {
            return null;
        }

        return article.articleTags.any().tag.name.eq(tag);
    }

    /**
     * 현재 로그인한 userId가 article.author를 팔로잉 중인지 확인
     */
    private BooleanExpression getFollowingExpression(Long userId) {
        if (userId == null) {
            return FALSE;
        }

        return JPAExpressions
                .selectOne()
                .from(follow)
                .where(
                        follow.fromUser.id.eq(userId),
                        follow.toUser.id.eq(article.author.id)
                )
                .exists();
    }
}
