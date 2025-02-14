package com.sparta.oishitable.global.util;

import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;
    private final long DURATION = Duration.ofDays(7).toMillis();

    public RedisUtil(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        if (valueOperations.get(key) == null) {
            throw new NotFoundException(ErrorCode.VALUE_NOT_FOUND_FOR_KEY);
        }
        return valueOperations.get(key);
    }

    public void setDataWithExpire(String key, String value) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, DURATION);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
