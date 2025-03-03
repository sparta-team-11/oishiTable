package com.sparta.oishitable.domain.common.user.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.response.QWaitingUserDetails;
import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.response.WaitingUserDetails;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sparta.oishitable.domain.common.user.entity.QUser.user;

@RequiredArgsConstructor
public class UserQRepositoryImpl implements UserQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<WaitingUserDetails> findWaitingUserDetails(List<Long> userIds) {
        String users = String.join(",",
                userIds.stream()
                        .map(String::valueOf)
                        .toList());

        String orderByField = "FIELD(user.id, " + users + ")";

        return queryFactory
                .select(new QWaitingUserDetails(
                        user.id,
                        user.name,
                        user.phoneNumber
                ))
                .from(user)
                .where(user.id.in(userIds))
                .orderBy(
                        Expressions.stringTemplate(orderByField)
                                .asc()
                )
                .fetch();
    }
}
