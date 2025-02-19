package com.sparta.oishitable.domain.customer.coupon.dto;

import jakarta.validation.constraints.NotNull;

public record CouponCreateRequest(
        @NotNull
        Long restaurantId,

        @NotNull
        Integer discount,

        @NotNull
        Long userId
) {}
