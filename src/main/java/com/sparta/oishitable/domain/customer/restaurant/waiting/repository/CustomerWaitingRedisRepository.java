package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import java.util.Optional;

public interface CustomerWaitingRedisRepository {

    void join(String queueKey, Long userId, Integer score);

    Optional<Integer> zFindLastSequence(String key);

    Optional<Long> zFindUserRank(String key, Long userId);

    void zRemove(String key, Long userId);

    Long zCard(String key);
}
