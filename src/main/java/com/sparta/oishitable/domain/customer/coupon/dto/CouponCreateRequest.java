package com.sparta.oishitable.domain.customer.coupon.dto;

public record CouponCreateRequest(
        Long restaurantId,
        Integer discount
) {}
