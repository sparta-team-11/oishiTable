package com.sparta.oishitable.domain.customer.coupon.dto;

import com.sparta.oishitable.domain.customer.coupon.entity.UserCoupon;
import jakarta.validation.constraints.NotNull;

public record UserCouponResponse(
        @NotNull
        Long couponId,

        @NotNull
        String couponName,

        @NotNull
        Integer discount,

        @NotNull
        Boolean couponUsed
) {

    public static UserCouponResponse from(UserCoupon usercoupon) {
        return new UserCouponResponse(
                usercoupon.getId(),
                usercoupon.getCoupon().getCouponName(),
                usercoupon.getCoupon().getDiscount(),
                usercoupon.getCouponUsed()
        );
    }
}