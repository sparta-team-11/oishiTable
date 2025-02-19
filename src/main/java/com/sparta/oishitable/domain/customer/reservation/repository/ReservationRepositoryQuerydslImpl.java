package com.sparta.oishitable.domain.customer.reservation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.customer.reservation.entity.Reservation;
import com.sparta.oishitable.domain.customer.reservation.entity.ReservationStatus;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.sparta.oishitable.domain.customer.reservation.entity.QReservation.reservation;

@RequiredArgsConstructor
public class ReservationRepositoryQuerydslImpl implements ReservationRepositoryQuerydsl {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Reservation> findReservedReservationById(Long reservationId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(reservation)
                        .where(
                                reservation.id.eq(reservationId),
                                reservation.status.eq(ReservationStatus.RESERVED)
                        )
                        .fetchOne());
    }
}
