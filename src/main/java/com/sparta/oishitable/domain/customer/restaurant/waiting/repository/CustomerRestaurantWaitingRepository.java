package com.sparta.oishitable.domain.customer.restaurant.waiting.repository;

import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRestaurantWaitingRepository extends JpaRepository<Waiting, Long>, CustomerRestaurantWaitingQueryDslRepository {
}
