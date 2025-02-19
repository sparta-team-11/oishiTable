package com.sparta.oishitable.domain.customer.coupon.entity;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Coupons")
@Getter

public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount", nullable = false)
    private Integer discount;

    @Column(name = "coupon_used", nullable = false)
    private Boolean couponUsed;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name =  "user_id", nullable = false)
    private User user;


    @Builder
    public Coupon(
            Integer discount,
            Boolean couponUsed,
            Restaurant restaurant,
            User user
    ) {
        this.discount = discount;
        this.couponUsed = couponUsed;
        this.restaurant = restaurant;
        this.user = user;
    }

    public void assignUser(User user) {
        this.user = user;
    }

}
