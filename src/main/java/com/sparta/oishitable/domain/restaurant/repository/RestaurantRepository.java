package com.sparta.oishitable.domain.restaurant.repository;

import com.sparta.oishitable.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
