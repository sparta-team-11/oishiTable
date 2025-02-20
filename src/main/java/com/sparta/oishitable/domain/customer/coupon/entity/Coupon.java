package com.sparta.oishitable.domain.customer.coupon.entity;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCoupon> userCoupons = new ArrayList<>();

    @Builder
    public Coupon(
            Integer discount,
            Boolean couponUsed,
            Restaurant restaurant
    ) {
        this.discount = discount;
        this.couponUsed = couponUsed;
        this.restaurant = restaurant;
    }

    public void setCouponUsed(Boolean couponUsed){
        this.couponUsed = couponUsed;
    }


}
