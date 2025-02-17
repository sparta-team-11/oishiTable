package com.sparta.oishitable.domain.reservation.service;

import com.sparta.oishitable.domain.reservation.dto.ReservationCreateRequest;
import com.sparta.oishitable.domain.reservation.dto.ReservationFindResponse;
import com.sparta.oishitable.domain.reservation.entity.Reservation;
import com.sparta.oishitable.domain.reservation.entity.ReservationStatus;
import com.sparta.oishitable.domain.reservation.repository.ReservationRepository;
import com.sparta.oishitable.domain.restaurantseat.entity.RestaurantSeat;
import com.sparta.oishitable.domain.restaurantseat.repository.RestaurantSeatRepository;
import com.sparta.oishitable.domain.restaurantseat.service.RestaurantSeatService;
import com.sparta.oishitable.domain.seatType.entity.SeatType;
import com.sparta.oishitable.domain.seatType.repository.SeatTypeRepository;
import com.sparta.oishitable.domain.seatType.service.SeatTypeService;
import com.sparta.oishitable.domain.user.entity.User;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RestaurantSeatService restaurantSeatService;
    private final SeatTypeService seatTypeService;

    private static final int couponQuantity = 100;

    @Transactional
    public void createReservationService(
            User user,
            ReservationCreateRequest request
    ) {

        SeatType seatType = seatTypeService.findSeatTypeByName(request.seatTypeName());

        RestaurantSeat restaurantSeat = restaurantSeatService.findBySeatType(seatType);

        LocalDateTime reservationDate = request.date();

        //같은 날짜에 같은 좌석에 예약되있는 건 전부를 조회
        int reservedCount = reservationRepository.countByRestaurantSeatAndDate(restaurantSeat, reservationDate);

        //가게 좌석의 수량과 비교한 후 자리가 없으면 에외
        if (reservedCount + request.totalCount() > restaurantSeat.getQuantity()) {
            throw new CustomRuntimeException(ErrorCode.RESTAURANT_SEAT_TYPE_QUANTITY_NOT_FOUND);
        }

//        Long couponProvided = reservationRepository.countByDiscount(10);
        long couponProvided = reservationRepository.countByCouponExistTrue();
        if (couponProvided >= couponQuantity) {
            throw new CustomRuntimeException(ErrorCode.RESERVATION_COUPON_LIMIT_EXCEEDED);
        }

        try {
            Reservation reservation = Reservation.builder()
                    .date(request.date())
                    .totalCount(request.totalCount())
                    .status(ReservationStatus.RESERVED)
                    .user(user)
                    .restaurantSeat(restaurantSeat)
                    .discount(10)
                    .couponExist(true)
                    .build();

            reservationRepository.save(reservation);

        } catch (OptimisticLockException e) {
            throw new CustomRuntimeException(ErrorCode.RESERVATION_CONFLICT);
        }

/*        Reservation reservation = Reservation.builder()
                .date(request.date())
                .totalCount(request.totalCount())
                .status(ReservationStatus.RESERVED)
                .user(user)
                .restaurantSeat(restaurantSeat)
                .discount(10)
                .couponExist(true)
                .build();

        reservationRepository.save(reservation);
        reservation.discountCoupon(10);

        reservationRepository.save(reservation);*/


    }

    public ReservationFindResponse findReservation(long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESERVATION_NOT_FOUND));

        reservation.complete();

        return new ReservationFindResponse(
                reservation.getRestaurantSeat().getId(),
                reservation.getDate(),
                reservation.getTotalCount(),
                reservation.getRestaurantSeat().getSeatType().getName(),
                reservation.getStatus(),
                reservation.isCouponExist()
        );

    }

    public List<ReservationFindResponse> findAllReservations(Long userId) {

        List<Reservation> reservations = reservationRepository.findByUser_Id(userId);

        return reservations.stream()
                .map(reservation -> new ReservationFindResponse(
                        reservation.getRestaurantSeat().getId(),
                        reservation.getDate(),
                        reservation.getTotalCount(),
                        reservation.getRestaurantSeat().getSeatType().getName(),
                        reservation.getStatus(),
                        reservation.isCouponExist()))
                .collect(Collectors.toList());

    }

    public void deleteReservation(long reservationId) {
//        reservationRepository.deleteById(reservationId);

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESERVATION_NOT_FOUND));

        reservation.cancel();

        reservationRepository.save(reservation);
    }
}
