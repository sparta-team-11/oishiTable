package com.sparta.oishitable.domain.customer.coupon.dto;

import com.sparta.oishitable.domain.customer.coupon.entity.Coupon;
import jakarta.validation.constraints.NotNull;

public record CouponResponse(
        @NotNull
        Long id,

        @NotNull
        Integer discount,

        @NotNull
        Long restaurantId

) {

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getDiscount(),
                coupon.getRestaurant().getId()
        );
    }

    public static CouponResponse from(Coupon coupon, Long userId) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getDiscount(),
                coupon.getRestaurant().getId()
        );
    }
}
