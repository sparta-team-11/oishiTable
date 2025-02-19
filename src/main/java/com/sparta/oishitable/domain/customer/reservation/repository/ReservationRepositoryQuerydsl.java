package com.sparta.oishitable.domain.customer.reservation.repository;

import com.sparta.oishitable.domain.customer.reservation.entity.Reservation;

import java.util.Optional;

public interface ReservationRepositoryQuerydsl {

    Optional<Reservation> findReservedReservationById(Long reservationId);
}
