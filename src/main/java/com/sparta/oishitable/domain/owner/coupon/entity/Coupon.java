package com.sparta.oishitable.domain.owner.coupon.entity;

import com.sparta.oishitable.domain.customer.coupon.entity.UserCoupon;
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
@Table(name = "coupons")
@Getter

public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount", nullable = false)
    private Integer discount;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCoupon> userCoupons = new ArrayList<>();

    @Builder
    public Coupon(
            Integer discount,
            Restaurant restaurant
    ) {
        this.discount = discount;
        this.restaurant = restaurant;
    }
}
