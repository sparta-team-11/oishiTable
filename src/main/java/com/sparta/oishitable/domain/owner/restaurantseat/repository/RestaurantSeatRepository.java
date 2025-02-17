package com.sparta.oishitable.domain.owner.restaurantseat.repository;

import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurantseat.entity.RestaurantSeat;
import com.sparta.oishitable.domain.admin.seatType.entity.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantSeatRepository extends JpaRepository<RestaurantSeat, Long> {

    Optional<RestaurantSeat> findBySeatType(SeatType seatType);

    void deleteByRestaurant(Restaurant restaurant);
}
