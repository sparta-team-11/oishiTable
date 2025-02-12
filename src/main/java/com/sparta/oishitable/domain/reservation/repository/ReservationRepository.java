package com.sparta.oishitable.domain.reservation.repository;

import com.sparta.oishitable.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
