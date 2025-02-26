package com.sparta.oishitable.domain.customer.follow.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.sparta.oishitable.domain.customer.follow.entity.QFollow.follow;

@RequiredArgsConstructor
public class FollowRepositoryQuerydslImpl implements FollowRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countFollowing(Long userId) {
        return Optional.ofNullable(queryFactory.select(follow.count())
                        .from(follow)
                        .where(follow.follower.id.eq(userId))
                        .fetchOne())
                .orElse(0L);
    }

    @Override
    public long countFollower(Long userId) {
        return Optional.ofNullable(queryFactory.select(follow.count())
                        .from(follow)
                        .where(follow.following.id.eq(userId))
                        .fetchOne())
                .orElse(0L);
    }
}
