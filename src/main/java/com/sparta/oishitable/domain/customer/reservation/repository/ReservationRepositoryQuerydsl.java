package com.sparta.oishitable.domain.customer.reservation.repository;

import com.sparta.oishitable.domain.customer.reservation.entity.Reservation;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReservationRepositoryQuerydsl {

    long countReservedReservationByRestaurantSeatAndDate(Long restaurantSeatId, LocalDateTime date);

    Optional<Reservation> findReservedReservationById(Long reservationId);
}
