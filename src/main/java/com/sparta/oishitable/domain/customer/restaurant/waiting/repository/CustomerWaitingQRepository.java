package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import java.util.Optional;

public interface CustomerWaitingQRepository {

    Optional<Integer> findTodayLastSequence(Long restaurantId);
}
