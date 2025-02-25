package com.sparta.oishitable.domain.owner.coupon.dto.request;

import jakarta.validation.constraints.NotNull;

public record CouponCreateRequest(
        @NotNull
        Integer discount

) {}
