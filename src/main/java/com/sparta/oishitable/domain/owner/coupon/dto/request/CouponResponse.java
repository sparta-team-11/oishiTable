package com.sparta.oishitable.domain.owner.coupon.dto.request;

import com.sparta.oishitable.domain.owner.coupon.entity.Coupon;
import jakarta.validation.constraints.NotNull;

public record CouponResponse(
        @NotNull
        Long id,

        @NotNull
        String couponName,

        @NotNull
        Integer discount,

        @NotNull
        Long restaurantId
) {

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCouponName(),
                coupon.getDiscount(),
                coupon.getRestaurant().getId()
        );
    }

    public static CouponResponse from(Coupon coupon, Long userId) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCouponName(),
                coupon.getDiscount(),
                coupon.getRestaurant().getId()
        );
    }
}
