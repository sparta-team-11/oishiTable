package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingRedisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Repository
@RequiredArgsConstructor
public class CustomerWaitingRedisRepositoryImpl implements CustomerWaitingRedisRepository {

    private final RedisTemplate<String, WaitingRedisDto> redisTemplate;
    private static final String WAITING_QUEUE_PREFIX = "restaurant_waiting_queue:";

    @Override
    public void push(Long restaurantId, WaitingRedisDto waitingRedisDto) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        redisTemplate.opsForList().rightPush(key, waitingRedisDto);
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
        WaitingRedisDto target = redisTemplate.opsForList().index(key, idx);

        redisTemplate.opsForList().remove(key, 1, target);
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
        return redisTemplate.opsForList().size(key);
    }

    private List<WaitingRedisDto> findQueue(Long restaurantId) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
