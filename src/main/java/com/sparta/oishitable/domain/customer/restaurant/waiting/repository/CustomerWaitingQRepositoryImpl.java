package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingType;
import com.sparta.oishitable.global.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.sparta.oishitable.domain.owner.restaurant.waiting.entity.QWaiting.waiting;

@Slf4j
@RequiredArgsConstructor
public class CustomerWaitingQRepositoryImpl implements CustomerWaitingQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Integer> findTodayLastSequence(Long restaurantId, WaitingType waitingType) {
        LocalDateTime startOfToday = DateTimeUtil.getStartOfToday();
        LocalDateTime endOfToday = DateTimeUtil.getEndOfToday();

        return Optional.ofNullable(queryFactory
                .select(
                        waiting.dailySequence
                )
                .from(waiting)
                .where(
                        waiting.restaurant.id.eq(restaurantId),
                        waiting.createdAt.between(startOfToday, endOfToday),
                        waiting.type.eq(waitingType)
                )
                .orderBy(waiting.createdAt.desc())
                .fetchFirst());
    }
}
