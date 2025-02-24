package com.sparta.oishitable.domain.owner.restaurant.repository;

import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRestaurantRepository extends JpaRepository<Restaurant, Long> {
}
