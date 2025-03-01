package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingRedisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

@Repository
@RequiredArgsConstructor
public class CustomerWaitingRedisRepositoryImpl implements CustomerWaitingRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, WaitingRedisDto> redisWaitingTemplate;
    private static final String WAITING_QUEUE_PREFIX = "restaurant_waiting_queue:";

    @Override
    public void push(Long restaurantId, WaitingRedisDto waitingRedisDto) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        redisWaitingTemplate.opsForList().rightPush(key, waitingRedisDto);
    }

    @Override
    public Optional<WaitingRedisDto> findUser(Long restaurantId, Long userId) {
        List<WaitingRedisDto> queue = findQueue(restaurantId);

        if (queue == null || queue.isEmpty()) {
            return Optional.empty();
        }

        return queue.stream()
                .filter(w -> w.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public void remove(Long restaurantId, Long idx) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        WaitingRedisDto target = redisWaitingTemplate.opsForList().index(key, idx);

        redisWaitingTemplate.opsForList().remove(key, 1, target);
    }

    @Override
    public Optional<Long> findUserRank(Long userId, Long restaurantId) {
        List<WaitingRedisDto> queue = findQueue(restaurantId);

        if (queue == null || queue.isEmpty()) {
            return Optional.empty();
        }

        return IntStream.range(0, queue.size())
                .filter(i -> queue.get(i).getUserId().equals(userId))
                .mapToObj(i -> (long) i)
                .findFirst();
    }

    @Override
    public Long findQueueSize(Long restaurantId) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        return redisWaitingTemplate.opsForList().size(key);
    }

    private List<WaitingRedisDto> findQueue(Long restaurantId) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        return redisWaitingTemplate.opsForList().range(key, 0, -1);
    }

    @Override
    public void zAdd(Long restaurantId, Long userId, Integer sequence) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        redisTemplate.opsForZSet().add(key, userId.toString(), sequence);
    }

    @Override
    public Optional<Integer> zFindLastSequence(Long restaurantId) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        Set<ZSetOperations.TypedTuple<String>> records = redisTemplate.opsForZSet().reverseRangeWithScores(key, 1, -1);

        if (records == null || records.isEmpty()) {
            return Optional.empty();
        }

        Double score = records.iterator().next().getScore();
        return Optional.of(score.intValue());
    }

    @Override
    public Optional<Long> zFindUserRank(Long restaurantId, Long userId) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        return Optional.ofNullable(redisTemplate.opsForZSet().rank(key, userId.toString()));
    }
}
