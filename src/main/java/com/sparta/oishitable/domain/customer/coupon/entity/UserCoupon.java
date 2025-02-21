package com.sparta.oishitable.domain.customer.coupon.entity;

import com.sparta.oishitable.domain.common.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "UserCoupons")
@Getter
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_used", nullable = false)
    private Boolean couponUsed = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Builder
    public UserCoupon(Long id, Boolean couponUsed, User user, Coupon coupon) {
        this.id = id;
        this.couponUsed = couponUsed;
        this.user = user;
        this.coupon = coupon;
    }

    public void setCouponUsed(Boolean couponUsed) {
        this.couponUsed = couponUsed;
    }


}
