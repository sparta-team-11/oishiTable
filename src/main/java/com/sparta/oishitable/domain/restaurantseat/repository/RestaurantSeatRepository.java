package com.sparta.oishitable.domain.restaurantseat.repository;

import com.sparta.oishitable.domain.restaurantseat.entity.RestaurantSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantSeatRepository extends JpaRepository<RestaurantSeat, Long> {
}
