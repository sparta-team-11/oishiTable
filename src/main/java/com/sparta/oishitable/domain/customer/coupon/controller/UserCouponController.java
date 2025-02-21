package com.sparta.oishitable.domain.customer.coupon.controller;

import com.sparta.oishitable.domain.customer.coupon.dto.UserCouponResponse;
import com.sparta.oishitable.domain.customer.coupon.service.UserCouponService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/api/{userId}/coupons")
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService usercouponService;

    @PostMapping("/{couponId}/download")
    public ResponseEntity<UserCouponResponse> downloadCoupon(
            @PathVariable Long couponId,
            @AuthenticationPrincipal CustomUserDetails userDetails

    ) {

        return ResponseEntity.ok(usercouponService.downloadCoupon(userDetails.getId(),couponId));
    }

    @GetMapping
    public ResponseEntity<List<UserCouponResponse>> findUserCoupons(
            @AuthenticationPrincipal CustomUserDetails userDetails

    ) {
        return ResponseEntity.ok(usercouponService.findUserCoupons(userDetails.getId()));
    }

    @PostMapping("/{couponId}/use")
    public ResponseEntity<UserCouponResponse> useCoupon(
            @PathVariable Long couponId,
            @AuthenticationPrincipal CustomUserDetails userDetails

    ) {

        return ResponseEntity.ok(usercouponService.useCoupon(userDetails.getId(), couponId));
    }

}