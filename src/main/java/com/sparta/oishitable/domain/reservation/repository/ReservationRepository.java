package com.sparta.oishitable.domain.reservation.repository;

import com.sparta.oishitable.domain.reservation.entity.Reservation;
import com.sparta.oishitable.domain.restaurantseat.entity.RestaurantSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    int countByRestaurantSeatAndDate(RestaurantSeat restaurantSeat, LocalDateTime reservationDate);

    List<Reservation> findByUser_Id(Long userId);

    long countByCouponExistTrue();

//    long countByDiscount(int discount);
}

