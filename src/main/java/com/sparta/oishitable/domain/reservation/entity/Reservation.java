package com.sparta.oishitable.domain.reservation.entity;

import com.sparta.oishitable.domain.common.BaseEntity;
import com.sparta.oishitable.domain.restaurantseat.entity.RestaurantSeat;
import com.sparta.oishitable.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(nullable = false)
    private Integer totalCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_seat_id")
    private RestaurantSeat restaurantSeat;

    @Version  // 낙관적 락
    private int version;

    private Integer discount = 0;

    @Column(nullable = false)
    private boolean couponExist = false; // 쿠폰 발급


    @Builder
    public Reservation(
            Long id,
            LocalDateTime date,
            ReservationStatus status,
            Integer totalCount,
            User user,
            RestaurantSeat restaurantSeat,
            Integer discount,
            boolean couponExist
    ) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.totalCount = totalCount;
        this.user = user;
        this.restaurantSeat = restaurantSeat;
        this.discount = discount;
        this.couponExist = couponExist;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELED;
    }

    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }

    public void discountCoupon(int discount) {
        this.discount = discount;
        this.couponExist = true;
    }
}
