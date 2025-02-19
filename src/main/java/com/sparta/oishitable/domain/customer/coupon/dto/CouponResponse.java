package com.sparta.oishitable.domain.customer.coupon.dto;

import com.sparta.oishitable.domain.customer.coupon.entity.Coupon;
import jakarta.validation.constraints.NotNull;

public record CouponResponse(
        @NotNull
        Long id,

        @NotNull
        Integer discount,

        @NotNull
        Boolean couponUsed,

        @NotNull
        Long restaurantId,

        @NotNull
        Long userId
) {

        public static CouponResponse from(Coupon coupon) {
                return new CouponResponse(
                        coupon.getId(),
                        coupon.getDiscount(),
                        coupon.getCouponUsed(),
                        coupon.getRestaurant().getId(),
                        coupon.getUser() != null ? coupon.getUser().getId() : null
                );
        }
}
