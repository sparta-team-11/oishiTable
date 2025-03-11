package com.sparta.oishitable.domain.owner.restaurant.waiting.repository;

import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRestaurantWaitingRepository extends JpaRepository<Waiting, Long>, OwnerRestaurantWaitingQRepository {
}
