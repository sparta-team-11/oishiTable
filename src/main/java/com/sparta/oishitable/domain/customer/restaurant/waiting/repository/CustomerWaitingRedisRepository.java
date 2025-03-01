package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingRedisDto;

import java.util.Optional;

public interface CustomerWaitingRedisRepository {

    void push(Long restaurantId, WaitingRedisDto waitingRedisDto);

    Optional<WaitingRedisDto> findUser(Long restaurantId, Long userId);

    void remove(Long restaurantId, Long idx);

    Optional<Long> findUserRank(Long userId, Long restaurantId);

    Long findQueueSize(Long restaurantId);
}
