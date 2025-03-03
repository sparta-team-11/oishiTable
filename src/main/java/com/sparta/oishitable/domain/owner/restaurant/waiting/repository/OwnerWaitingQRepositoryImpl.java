package com.sparta.oishitable.domain.owner.restaurant.waiting.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.response.QWaitingDetails;
import com.sparta.oishitable.domain.owner.restaurant.waiting.dto.response.WaitingDetails;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.sparta.oishitable.domain.common.user.entity.QUser.user;
import static com.sparta.oishitable.domain.owner.restaurant.waiting.entity.QWaiting.waiting;

@RequiredArgsConstructor
public class OwnerWaitingQRepositoryImpl implements OwnerWaitingQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<WaitingDetails> findWaitingDetails(List<Long> userIds) {
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfToday = startOfToday.plusDays(1).minusNanos(1);

        return queryFactory.select(
                        new QWaitingDetails(
                                waiting.id,
                                waiting.user.id,
                                waiting.user.name,
                                waiting.totalCount,
                                waiting.user.phoneNumber,
                                waiting.status,
                                waiting.type
                        )
                )
                .from(waiting)
                .innerJoin(waiting.user, user)
                .where(
                        waiting.user.id.in(userIds),
                        waiting.createdAt.between(startOfToday, endOfToday)
                )
                .orderBy(waiting.createdAt.asc())
                .fetch();
    }
}
