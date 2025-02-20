package com.sparta.oishitable.domain.customer.reservation.repository;

import com.sparta.oishitable.domain.customer.reservation.entity.Reservation;
import com.sparta.oishitable.domain.owner.restaurantseat.entity.RestaurantSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryQuerydsl {

    int countByRestaurantSeatAndDate(RestaurantSeat restaurantSeat, LocalDateTime reservationDate);

    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);
}
