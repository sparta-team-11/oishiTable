package com.sparta.oishitable.domain.customer.coupon.dto;

public record CouponResponse(
        Long id,
        Integer discount,
        Boolean couponUsed,
        Long restaurantId,
        Long userId
) {}
