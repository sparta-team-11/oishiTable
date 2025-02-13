package com.sparta.oishitable.domain.reservation.service;

import com.sparta.oishitable.domain.reservation.dto.UserCreateRequest;
import com.sparta.oishitable.domain.reservation.entity.Reservation;
import com.sparta.oishitable.domain.reservation.entity.ReservationStatus;
import com.sparta.oishitable.domain.reservation.repository.ReservationRepository;
import com.sparta.oishitable.domain.restaurantseat.entity.RestaurantSeat;
import com.sparta.oishitable.domain.restaurantseat.repository.RestaurantSeatRepository;
import com.sparta.oishitable.domain.seatType.entity.SeatType;
import com.sparta.oishitable.domain.seatType.repository.SeatTypeRepository;
import com.sparta.oishitable.domain.user.entity.User;
import com.sparta.oishitable.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final RestaurantSeatRepository restaurantSeatRepository;


    @Transactional
    public void createReservationService(
            User user,
            UserCreateRequest request
    ) {

        SeatType seatType = seatTypeRepository.findBySeatTypeName(request.seatTypeName())
                .orElseThrow(() -> new NotFoundException("좌석 타입이 없습니다"));

        RestaurantSeat restaurantSeat = restaurantSeatRepository.findBySeatType(seatType)
                .orElseThrow(() -> new NotFoundException("좌석이 없습니다."));


        Reservation reservation = Reservation.builder()
                .date(request.date())
                .totalCount(request.totalCount())
                .status(ReservationStatus.COMPLETED)
                .user(user)
                .restaurantSeat(restaurantSeat)
                .build();

        reservationRepository.save(reservation);

    }
}
