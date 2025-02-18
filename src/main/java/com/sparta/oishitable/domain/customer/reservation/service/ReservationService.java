package com.sparta.oishitable.domain.customer.reservation.service;

import com.sparta.oishitable.domain.customer.coupon.entity.Coupon;
import com.sparta.oishitable.domain.customer.coupon.repository.CouponRepository;
import com.sparta.oishitable.domain.customer.reservation.dto.ReservationCreateRequest;
import com.sparta.oishitable.domain.customer.reservation.dto.ReservationFindResponse;
import com.sparta.oishitable.domain.customer.reservation.entity.Reservation;
import com.sparta.oishitable.domain.customer.reservation.entity.ReservationStatus;
import com.sparta.oishitable.domain.customer.reservation.repository.ReservationRepository;
import com.sparta.oishitable.domain.owner.restaurantseat.entity.RestaurantSeat;
import com.sparta.oishitable.domain.owner.restaurantseat.service.RestaurantSeatService;
import com.sparta.oishitable.domain.admin.seatType.entity.SeatType;
import com.sparta.oishitable.domain.admin.seatType.service.SeatTypeService;
import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final RestaurantSeatService restaurantSeatService;
    private final SeatTypeService seatTypeService;
    private final CouponRepository couponRepository;

//    private static final int couponQuantity = 100;

    @Transactional
    public Long createReservationService(
            Long userId,
            ReservationCreateRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        SeatType seatType = seatTypeService.findSeatTypeByName(request.seatTypeName());

        RestaurantSeat restaurantSeat = restaurantSeatService.findBySeatType(seatType);

        LocalDateTime reservationDate = request.date();

        //같은 날짜에 같은 좌석에 예약되있는 건 전부를 조회
        int reservedCount = reservationRepository.countByRestaurantSeatAndDate(restaurantSeat, reservationDate);

        //가게 좌석의 수량과 비교한 후 자리가 없으면 에외
        if (reservedCount + request.totalCount() > restaurantSeat.getQuantity()) {
            throw new CustomRuntimeException(ErrorCode.RESTAURANT_SEAT_TYPE_QUANTITY_NOT_FOUND);
        }

        //가게별로 쿠폰을 가져온다.
        List<Coupon> listCoupons = couponRepository.findByRestaurantId(restaurantSeat.getRestaurant().getId());

        Coupon eventCoupon = null;

        //가게별로 쿠폰중에 Discount 쿠폰을 가져온다
        for (Coupon availableCoupon : listCoupons) {
            if (availableCoupon.getDiscount() > 0  && availableCoupon.getCouponProvided() > 0) {
                eventCoupon = availableCoupon;
                break;
            }
        }

        //eventCoupon
        if (eventCoupon != null) {
            long usedCouponCount = reservationRepository.countByUser_IdAndCouponIsNotNull(userId/*, eventCoupon.getId()*/);
            if (usedCouponCount > 0) {
                eventCoupon = null;
            }
        }

        if (eventCoupon != null) {
            eventCoupon.setCouponProvided(eventCoupon.getCouponProvided() - 1);
            couponRepository.save(eventCoupon);
        }

/*        eventCoupon.setCouponProvided(eventCoupon.getCouponProvided() - 1);

        eventCoupon.setCouponExist(true);

        couponRepository.save(eventCoupon);*/

        try {
            Reservation reservation = Reservation.builder()
                    .date(request.date())
                    .totalCount(request.totalCount())
                    .status(ReservationStatus.RESERVED)
                    .user(user)
                    .restaurantSeat(restaurantSeat)
                    .coupon(eventCoupon)
                    .build();

            reservationRepository.save(reservation);

            return reservation.getId();

        } catch (OptimisticLockException e) {
            throw new CustomRuntimeException(ErrorCode.RESERVATION_CONFLICT);
        }

    }

    public ReservationFindResponse findReservation(long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESERVATION_NOT_FOUND));

        reservation.complete();

        boolean couponProvided = (reservation.getCoupon() != null && reservation.getCoupon().getCouponExist());

        return new ReservationFindResponse(
                reservation.getRestaurantSeat().getId(),
                reservation.getDate(),
                reservation.getTotalCount(),
                reservation.getRestaurantSeat().getSeatType().getName(),
                reservation.getStatus(),
                couponProvided
        );
    }

    public List<ReservationFindResponse> findAllReservations(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUser_Id(userId);


        return reservations.stream()
                .map(reservation -> {
                    boolean couponProvided = (reservation.getCoupon() != null && reservation.getCoupon().getCouponExist());

                    return new ReservationFindResponse(
                            reservation.getRestaurantSeat().getId(),
                            reservation.getDate(),
                            reservation.getTotalCount(),
                            reservation.getRestaurantSeat().getSeatType().getName(),
                            reservation.getStatus(),
                            couponProvided
                            );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReservation(long reservationId) {
//        reservationRepository.deleteById(reservationId);
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.RESERVATION_NOT_FOUND));

        reservation.cancel();
    }
}
