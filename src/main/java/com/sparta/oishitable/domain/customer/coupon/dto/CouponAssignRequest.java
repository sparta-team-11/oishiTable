package com.sparta.oishitable.domain.customer.coupon.dto;

public record CouponAssignRequest(
        Long couponId,
        Long userId
) {}
