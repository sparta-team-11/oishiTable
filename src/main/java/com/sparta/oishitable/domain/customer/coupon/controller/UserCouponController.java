package com.sparta.oishitable.domain.customer.coupon.controller;

import com.sparta.oishitable.domain.customer.coupon.dto.UserCouponResponse;
import com.sparta.oishitable.domain.customer.coupon.service.UserCouponService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import com.sparta.oishitable.global.util.UriBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customer/api/coupons")
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService usercouponService;

    @PostMapping("/{couponId}/download")
    public ResponseEntity<Void> downloadCoupon(
            @PathVariable Long couponId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        Long userCouponId = usercouponService.downloadCoupon(userDetails.getId(), couponId);

        URI location = UriBuilderUtil.create("/customer/api/coupons", userCouponId);

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<UserCouponResponse>> findUserCoupons(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size

    ) {
        return ResponseEntity.ok(usercouponService.findUserCoupons(userDetails.getId(), cursor, size));
    }

    @DeleteMapping("/{couponId}/use")
    public ResponseEntity<Void> useCoupon(
            @PathVariable Long couponId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        usercouponService.useCoupon(userDetails.getId(), couponId);

        return ResponseEntity.ok().build();
    }
}