package com.sparta.oishitable.domain.owner.coupon.dto.request;

import com.sparta.oishitable.domain.owner.coupon.entity.CouponType;
import jakarta.validation.constraints.NotNull;

public record CouponCreateRequest(
        @NotNull
        String couponName,

        @NotNull
        Integer discount,

        @NotNull
        Integer firstComeCouponMaxCount,

        @NotNull
        CouponType type

) {
}
