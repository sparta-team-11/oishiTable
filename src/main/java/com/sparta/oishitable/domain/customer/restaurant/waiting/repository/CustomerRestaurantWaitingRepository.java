package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomerRestaurantWaitingRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String WAITING_QUEUE_PREFIX = "restaurant_waiting_queue:";

    public Long push(Long userId, Long restaurantId) {
        return redisTemplate.opsForList().rightPush(WAITING_QUEUE_PREFIX + restaurantId, userId.toString());
    }

    public Long remove(Long userId, Long restaurantId) {
        return redisTemplate.opsForList().remove(WAITING_QUEUE_PREFIX + restaurantId, 0, userId.toString());
    }

    public Optional<Long> findUserRank(Long userId, Long restaurantId) {
        return Optional.ofNullable(redisTemplate.opsForList().indexOf(WAITING_QUEUE_PREFIX + restaurantId, userId.toString()));
    }

    public Long findQueueSize(Long restaurantId) {
        return redisTemplate.opsForList().size(WAITING_QUEUE_PREFIX + restaurantId);
    }
}
