package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import java.util.Optional;

public interface CustomerWaitingRedisRepository {

    Long join(String queueKey, String sequenceKey, Long userId);

    Optional<Long> zFindUserRank(String key, Long userId);

    void zRemove(String key, Long userId);

    Long zCard(String key);
}
