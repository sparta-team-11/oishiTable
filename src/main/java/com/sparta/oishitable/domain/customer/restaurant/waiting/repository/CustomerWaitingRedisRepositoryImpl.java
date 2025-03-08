package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerWaitingRedisRepositoryImpl implements CustomerWaitingRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Long join(String queueKey, String sequenceKey, Long userId) {
        String script = """
                local queue_key = KEYS[1]
                local sequence_key = KEYS[2]
                local user_id = ARGV[1]
                
                -- 마지막 순번 INCR
                local last_seq = redis.call("INCR", sequence_key)
                
                -- 저장
                redis.call("ZADD", queue_key, last_seq, user_id)
                redis.call("SET", sequence_key, last_seq)
                
                return last_seq
                """;

        return redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                List.of(queueKey, sequenceKey),
                userId.toString());
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
