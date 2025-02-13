package com.sparta.oishitable.domain.reservation.service;

import com.sparta.oishitable.domain.reservation.dto.ReservationCreateRequest;
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

import java.time.LocalDateTime;

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
            ReservationCreateRequest request
    ) {

        SeatType seatType = seatTypeRepository.findBySeatTypeName(request.seatTypeName())
                .orElseThrow(() -> new NotFoundException("좌석 타입이 없습니다"));

        RestaurantSeat restaurantSeat = restaurantSeatRepository.findBySeatType(seatType)
                .orElseThrow(() -> new NotFoundException("좌석이 없습니다."));

        LocalDateTime reservationDate = request.date();

        //같은 날짜에 같은 좌석에 예약되있는 건 전부를 조회
        int reservedCount = reservationRepository.countByRestaurantSeatAndDate(restaurantSeat,reservationDate);

        //가게 좌석의 수량과 비교한 후 자리가 없으면 에외
        if(reservedCount + request.totalCount() > restaurantSeat.getQuantity()){
            throw new NotFoundException("다른 날짜에 예약을 해주세요");
        }

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
