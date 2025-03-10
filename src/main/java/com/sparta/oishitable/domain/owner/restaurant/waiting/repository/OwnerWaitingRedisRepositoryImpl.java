package com.sparta.oishitable.domain.owner.restaurant.waiting.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class OwnerWaitingRedisRepositoryImpl implements OwnerWaitingRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public List<Long> findWaitingUsers(String key, int page, int size) {
        long start = (long) page * size;
        long end = start + size - 1;

        Set<String> queue = redisTemplate.opsForZSet().range(key, start, end);

        if (queue == null || queue.isEmpty()) {
            return List.of();
        }

        return queue.stream()
                .map(Long::parseLong)
                .toList();
    }

    @Override
    public Long findQueueSize(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    @Override
    public Long deleteWaitingUser(String key, Long userId) {
        return redisTemplate.opsForZSet().remove(key, userId.toString());
    }
}
