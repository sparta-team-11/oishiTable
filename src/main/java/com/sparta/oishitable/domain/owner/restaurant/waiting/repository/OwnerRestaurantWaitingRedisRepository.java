package com.sparta.oishitable.domain.owner.restaurant.waiting.repository;

import java.util.List;

public interface OwnerRestaurantWaitingRedisRepository {

    List<Long> findWaitingUsers(String key, int page, int size);

    Long findQueueSize(String key);

    Long deleteWaitingUser(String key, Long userId);
}
