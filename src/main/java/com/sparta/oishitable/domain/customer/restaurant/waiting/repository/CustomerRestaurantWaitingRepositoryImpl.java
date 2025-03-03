package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRestaurantWaitingRepositoryImpl {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String WAITING_QUEUE_PREFIX = "restaurant_waiting_queue:";

    public void push(Long userId, Long restaurantId) {
        redisTemplate.opsForList().rightPush(WAITING_QUEUE_PREFIX + restaurantId, userId.toString());
    }

    public void remove(Long userId, Long restaurantId) {
        redisTemplate.opsForList().remove(WAITING_QUEUE_PREFIX + restaurantId, 1, userId.toString());
    }

    public Optional<Long> findUserRank(Long userId, Long restaurantId) {
        return Optional.ofNullable(redisTemplate.opsForList().indexOf(WAITING_QUEUE_PREFIX + restaurantId, userId.toString()));
    }

    public Long findQueueSize(Long restaurantId) {
        return redisTemplate.opsForList().size(WAITING_QUEUE_PREFIX + restaurantId);
    }
}
