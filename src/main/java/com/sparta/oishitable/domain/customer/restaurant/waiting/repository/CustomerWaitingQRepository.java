package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.WaitingType;

import java.util.Optional;

public interface CustomerWaitingQRepository {

    Optional<Integer> findTodayLastSequence(Long restaurantId, WaitingType waitingType);
}
