package com.sparta.oishitable.domain.customer.reservation.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.reservation.dto.ReservationCreateRequest;
import com.sparta.oishitable.domain.customer.reservation.dto.ReservationFindResponse;
import com.sparta.oishitable.domain.customer.reservation.entity.Reservation;
import com.sparta.oishitable.domain.customer.reservation.repository.ReservationRepository;
import com.sparta.oishitable.domain.owner.restaurantseat.entity.RestaurantSeat;
import com.sparta.oishitable.domain.owner.restaurantseat.service.RestaurantSeatService;
import com.sparta.oishitable.global.aop.annotation.DistributedLock;
import com.sparta.oishitable.global.exception.ForbiddenException;
import com.sparta.oishitable.global.exception.InvalidException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import com.sparta.oishitable.notification.entity.Notification;
import com.sparta.oishitable.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final RestaurantSeatService restaurantSeatService;
    private final NotificationRepository notificationRepository;

    @DistributedLock(key = "'reservation:' + #request.restaurantId + ':' + #formattedDate")
    public Long createReservation(Long userId, ReservationCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        RestaurantSeat restaurantSeat = restaurantSeatService.findByRestaurantIdAndSeatTypeId(
                request.restaurantId(),
                request.seatTypeId()
        );

        //같은 날짜에 같은 좌석에 예약되있는 건 전부를 조회
        long reservedCount = reservationRepository.countReservedReservationByRestaurantSeatAndDate(
                restaurantSeat.getId(), request.date()
        );

        //가게 좌석의 수량과 비교한 후 자리가 없으면 에외
        if (restaurantSeat.getQuantity() <= reservedCount) {
            throw new InvalidException(ErrorCode.RESTAURANT_SEAT_NOT_AVAILABLE);
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

        Notification notification = Notification.builder()
                .userId(reservation.getUser().getId())
                .message("예약이 확정되었습니다. 예약 ID: " + reservation.getId() + "\n예약 시간: " + reservation.getDate())
                .email(user.getEmail())
                .scheduledTime(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        reservationReminder(reservation,user, 24);
        reservationReminder(reservation,user, 1);

        return reservation.getId();
    }

    @Transactional(readOnly = true)
    public ReservationFindResponse findReservation(Long reservationId) {
        Reservation reservation = findReservationById(reservationId);

        return ReservationFindResponse.from(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationFindResponse> findReservations(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return reservations.stream()
                .map(ReservationFindResponse::from)
                .toList();
    }

    @Transactional
    public void deleteReservation(Long userId, Long reservationId) {
        Reservation reservation = findReservedReservationById(reservationId);

        if (reservation.getUser().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.USER_UNAUTHORIZED);
        }

        reservation.cancel();
    }

    private Reservation findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    private Reservation findReservedReservationById(Long reservationId) {
        return reservationRepository.findReservedReservationById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVED_RESERVATION_NOT_FOUND));
    }

    private void reservationReminder(Reservation reservation, User user, int hoursBefore) {

        LocalDateTime reminderTime = reservation.getDate().minusHours(hoursBefore);

        if (reminderTime.isAfter(LocalDateTime.now())) {

            String when = hoursBefore == 24 ? "하루 전 알림: " : hoursBefore + "시간 전 알림: ";

            Notification notification = Notification.builder()
                    .userId(reservation.getUser().getId())
                    .message("예약 " + when + "예약 시간은 " + reservation.getDate() + " 입니다.")
                    .email(user.getEmail())
                    .scheduledTime(reminderTime)
                    .build();

            notificationRepository.save(notification);
        }
    }
}
