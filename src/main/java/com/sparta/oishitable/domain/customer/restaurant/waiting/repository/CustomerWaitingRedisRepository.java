package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingRedisDto;

import java.util.Optional;

public interface CustomerWaitingRedisRepository {

    void push(Long restaurantId, WaitingRedisDto waitingRedisDto) throws JsonProcessingException;

    Optional<WaitingRedisDto> findUser(Long restaurantId, Long userId);

    void remove(Long userId, Long restaurantId);

    Optional<Long> findUserRank(Long userId, Long restaurantId);

    Long findQueueSize(Long restaurantId);
}
