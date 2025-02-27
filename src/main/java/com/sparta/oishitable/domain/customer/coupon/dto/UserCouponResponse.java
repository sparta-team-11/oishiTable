package com.sparta.oishitable.domain.customer.coupon.dto;

import com.sparta.oishitable.domain.customer.coupon.entity.UserCoupon;
import jakarta.validation.constraints.NotNull;

public record UserCouponResponse(
        @NotNull
        Long id,

        @NotNull
        String couponName,

        @NotNull
        String discount,

        @NotNull
        Boolean couponUsed,

        @NotNull
        Long restaurantId

) {

    public static UserCouponResponse from(UserCoupon usercoupon) {
        return new UserCouponResponse(
                usercoupon.getId(),
                usercoupon.getCoupon().getCouponName(),
                usercoupon.getCoupon().getDiscount() + "% 할인 쿠폰입니다.",
                usercoupon.getCouponUsed(),
                usercoupon.getCoupon().getRestaurant().getId()
        );
    }
}