package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerWaitingRedisRepositoryImpl implements CustomerWaitingRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void join(String queueKey, Long userId, double score) {
        redisTemplate.opsForZSet().add(queueKey, userId.toString(), score);
    }

    @Override
    public Optional<Long> zFindUserRank(String key, Long userId) {
        return Optional.ofNullable(redisTemplate.opsForZSet().rank(key, userId.toString()));
    }

    @Override
    public void zRemove(String key, Long userId) {
        redisTemplate.opsForZSet().remove(key, userId.toString());
    }

    @Override
    public Long zCard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }
}
