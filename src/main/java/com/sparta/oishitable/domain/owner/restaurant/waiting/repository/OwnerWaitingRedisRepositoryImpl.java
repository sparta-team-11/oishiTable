package com.sparta.oishitable.domain.owner.restaurant.waiting.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OwnerWaitingRedisRepositoryImpl {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String WAITING_QUEUE_PREFIX = "restaurant_waiting_queue:";

    public List<Long> findWaitingUsers(Long restaurantId, int page, int size) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;

        long start = (long) page * size;
        long end = start + size - 1;

        return redisTemplate.opsForList().range(key, start, end).stream()
                .map(Long::parseLong)
                .toList();
    }

    public Long findQueueSize(Long restaurantId) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;

        return redisTemplate.opsForList().size(key);
    }

    public void clearWaitingQueue(Long restaurantId) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;

        redisTemplate.delete(key);
    }

    public Long deleteUserFromWaitingQueue(Long userId, Long restaurantId) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;

        return redisTemplate.opsForList().remove(key, 1, userId.toString());
    }
}
