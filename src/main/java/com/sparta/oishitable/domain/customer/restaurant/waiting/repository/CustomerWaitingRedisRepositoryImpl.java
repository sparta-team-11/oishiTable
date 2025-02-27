package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingRedisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerWaitingRedisRepositoryImpl implements CustomerWaitingRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String WAITING_QUEUE_PREFIX = "restaurant_waiting_queue:";

    @Override
    public void push(Long restaurantId, WaitingRedisDto waitingRedisDto) throws JsonProcessingException {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        redisTemplate.opsForList().rightPush(key, objectMapper.writeValueAsString(waitingRedisDto));
    }

    @Override
    public Optional<WaitingRedisDto> findUser(Long restaurantId, Long userId) {
        List<Object> queue = findQueue(restaurantId);

        if (queue == null || queue.isEmpty()) {
            return Optional.empty();
        }

        return queue.stream()
                .filter(o -> {
                    try {
                        WaitingRedisDto dto = convertToDto(o);
                        return dto.getUserId().equals(userId);
                    } catch (JsonProcessingException e) {
                        return false;
                    }
                })
                .findFirst()
                .map(o -> {
                    try {
                        return convertToDto(o);
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                });
    }

    @Override
    public void remove(Long userId, Long restaurantId) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        redisTemplate.opsForList().remove(key, 1, userId.toString());
    }

    @Override
    public Optional<Long> findUserRank(Long userId, Long restaurantId) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        return Optional.ofNullable(redisTemplate.opsForList().indexOf(key, userId.toString()));
    }

    @Override
    public Long findQueueSize(Long restaurantId) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        return redisTemplate.opsForList().size(key);
    }

    private List<Object> findQueue(Long restaurantId) {
        String key = WAITING_QUEUE_PREFIX + restaurantId;
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    private WaitingRedisDto convertToDto(Object o) throws JsonProcessingException {
        return objectMapper.readValue(String.valueOf(o), WaitingRedisDto.class);
    }
}
