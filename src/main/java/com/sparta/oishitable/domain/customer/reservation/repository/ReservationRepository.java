package com.sparta.oishitable.domain.customer.reservation.repository;

import com.sparta.oishitable.domain.customer.reservation.entity.Reservation;
import com.sparta.oishitable.domain.owner.restaurantseat.entity.RestaurantSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    int countByRestaurantSeatAndDate(RestaurantSeat restaurantSeat, LocalDateTime reservationDate);

    List<Reservation> findByUser_Id(Long userId);

    Long countByUser_IdAndCouponIsNotNull(Long userId/*, Long couponId*/);


}

