package com.sparta.oishitable.domain.customer.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.customer.post.dto.response.PostKeywordResponse;
import com.sparta.oishitable.domain.customer.post.dto.response.PostRandomResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sparta.oishitable.domain.customer.post.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryQuerydslImpl implements PostRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostRandomResponse> findPostsByRandom(
            Long userId,
            Long regionId,
            Long cursorValue,
            int limit,
            int randomSeed
    ) {
        //TODO 팔로우 기능 추가되면 조건 넣기

        BooleanBuilder builder = new BooleanBuilder();

        // 지역값 null이면 전체, 있으면 특정 지역 검색
        if (regionId != null) {
            builder.and(post.region.id.eq(regionId));
        }

        // 각 행마다 post.id와 randomSeed를 조합하여 해시함수로 랜덤값 도출                                                                                  계산
        NumberExpression<Long> randomValue = Expressions.numberTemplate(Long.class,
                "MOD(CRC32(CONCAT({0}, {1})), {2})", post.id, randomSeed, 1000000000);

        // 만약 cursorValue 가 있으면 그 값보다 큰 행들 조회
        if (cursorValue != null) {
            builder.and(randomValue.gt(cursorValue));
        }

        return queryFactory
                .select(
                        Projections.constructor(PostRandomResponse.class,
                                post.id,
                                post.user.id,
                                post.region.id,
                                post.title,
                                post.content,
                                post.user.name,
                                post.modifiedAt,
                                randomValue
                        )
                )
                .from(post)
                .where(builder)
                // 계산한 랜덤 값들을 기준으로 오름차순으로 정렬 (만약 중복된다면 id 오름차순으로)
                .orderBy(randomValue.asc(), post.id.asc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<PostKeywordResponse> findPostsByKeyword(
            Long userId,
            Long regionId,
            Long cursorValue,
            String keyword,
            int limit
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        if (regionId != null) {
            builder.and(post.region.id.eq(regionId));
        }

        // 최신순으로 정렬해서 커서값(게시글 ID)보다 낮은 행들을 조회
        if (cursorValue != null) {
            builder.and(post.id.lt(cursorValue));
        }

        BooleanBuilder keywordBuilder = new BooleanBuilder();

        // 키워드 검색
        keywordBuilder.or(post.title.containsIgnoreCase(keyword))
                .or(post.content.containsIgnoreCase(keyword))
                .or(post.user.name.containsIgnoreCase(keyword));

        builder.and(keywordBuilder);

        return queryFactory
                .select(
                        Projections.constructor(PostKeywordResponse.class,
                                post.id,
                                post.user.id,
                                post.region.id,
                                post.title,
                                post.content,
                                post.user.name,
                                post.modifiedAt
                        )
                )
                .from(post)
                .where(builder)
                // 내림차순(최신순) 정렬
                .orderBy(post.id.desc())
                .limit(limit)
                .fetch();
    }
}
