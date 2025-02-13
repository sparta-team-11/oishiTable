package com.sparta.oishitable.domain.reservation.repository;

import com.sparta.oishitable.domain.reservation.entity.Reservation;
import com.sparta.oishitable.domain.restaurantseat.entity.RestaurantSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    int countByRestaurantSeatAndDate(RestaurantSeat restaurantSeat, LocalDateTime reservationDate);
}
