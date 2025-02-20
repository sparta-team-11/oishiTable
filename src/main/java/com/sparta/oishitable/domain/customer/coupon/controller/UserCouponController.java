package com.sparta.oishitable.domain.customer.coupon.controller;

import com.sparta.oishitable.domain.customer.coupon.dto.CouponResponse;
import com.sparta.oishitable.domain.customer.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer/api/{userId}/coupons")
@RequiredArgsConstructor
public class UserCouponController {

    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<List<CouponResponse>> findUserCoupons(@PathVariable Long userId) {
        return ResponseEntity.ok(couponService.findUserCoupons(userId));
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponse> findUserCoupon(
            @PathVariable Long userId,
            @PathVariable Long couponId
            ) {
        return ResponseEntity.ok(couponService.findUserCoupon(userId, couponId));
    }
}