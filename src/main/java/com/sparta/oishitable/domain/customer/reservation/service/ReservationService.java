package com.sparta.oishitable.domain.customer.reservation.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.reservation.dto.ReservationCreateRequest;
import com.sparta.oishitable.domain.customer.reservation.dto.ReservationFindResponse;
import com.sparta.oishitable.domain.customer.reservation.entity.Reservation;
import com.sparta.oishitable.domain.customer.reservation.repository.ReservationRepository;
import com.sparta.oishitable.domain.owner.restaurantseat.entity.RestaurantSeat;
import com.sparta.oishitable.domain.owner.restaurantseat.service.RestaurantSeatService;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.InvalidException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final RestaurantSeatService restaurantSeatService;

    @Transactional
    public void createReservationService(
            Long userId,
            ReservationCreateRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        RestaurantSeat restaurantSeat = restaurantSeatService.findByRestaurantIdAndSeatTypeId(
                request.restaurantId(),
                request.seatTypeId()
        );

        //같은 날짜에 같은 좌석에 예약되있는 건 전부를 조회
        int reservedCount = reservationRepository.countByRestaurantSeatAndDate(restaurantSeat, request.date());

        //가게 좌석의 수량과 비교한 후 자리가 없으면 에외
        if (restaurantSeat.getQuantity() <= reservedCount) {
            throw new InvalidException(ErrorCode.RESTAURANT_SEAT_TYPE_QUANTITY_NOT_FOUND);
        }

        if (restaurantSeat.getMinGuestCount() > request.totalCount()) {
            throw new InvalidException(ErrorCode.BELOW_MIN_GUEST_COUNT);
        }

        if (restaurantSeat.getMaxGuestCount() < request.totalCount()) {
            throw new InvalidException(ErrorCode.EXCEEDS_MAX_GUEST_COUNT);
        }

        Reservation reservation = Reservation.builder()
                .date(request.date())
                .totalCount(request.totalCount())
                .user(user)
                .restaurantSeat(restaurantSeat)
                .build();

        reservationRepository.save(reservation);
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
                reservation.getStatus()
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
                        reservation.getStatus()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReservation(long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESERVATION_NOT_FOUND));

        reservation.cancel();
    }
}
