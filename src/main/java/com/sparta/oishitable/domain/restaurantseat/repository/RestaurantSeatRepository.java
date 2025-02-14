package com.sparta.oishitable.domain.restaurantseat.repository;

import com.sparta.oishitable.domain.restaurantseat.entity.RestaurantSeat;
import com.sparta.oishitable.domain.seatType.entity.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantSeatRepository extends JpaRepository<RestaurantSeat, Long> {
    Optional<RestaurantSeat> findBySeatType(SeatType seatType);

}
