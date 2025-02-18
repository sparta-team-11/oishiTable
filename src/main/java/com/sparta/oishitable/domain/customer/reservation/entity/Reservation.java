package com.sparta.oishitable.domain.customer.reservation.entity;

import com.sparta.oishitable.domain.common.BaseEntity;
import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.customer.coupon.entity.Coupon;
import com.sparta.oishitable.domain.owner.restaurantseat.entity.RestaurantSeat;
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

    @OneToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Version  // 낙관적 락
    private int version;

//    private Integer discount = 0;
//
//    @Column(nullable = false)
//    private boolean couponExist = false; // 쿠폰 발급

    @Builder
    public Reservation(
            Long id,
            LocalDateTime date,
            ReservationStatus status,
            Integer totalCount,
            User user,
            RestaurantSeat restaurantSeat,
            Coupon coupon
    ) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.totalCount = totalCount;
        this.user = user;
        this.restaurantSeat = restaurantSeat;
        this.coupon = coupon;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELED;
    }

    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }

    public Long getRestaurantId(){
        return this.restaurantSeat.getRestaurant().getId();
    }

//    public void discountCoupon(int discount) {
//        this.discount = discount;
//        this.couponExist = true;
//    }
}
