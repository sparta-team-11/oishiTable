package com.sparta.oishitable.domain.customer.coupon.controller;

import com.sparta.oishitable.domain.customer.coupon.dto.CouponAssignRequest;
import com.sparta.oishitable.domain.customer.coupon.dto.CouponCreateRequest;
import com.sparta.oishitable.domain.customer.coupon.dto.CouponResponse;
import com.sparta.oishitable.domain.customer.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/create")
    public ResponseEntity<CouponResponse> createCoupon(@RequestBody CouponCreateRequest request) {
        return ResponseEntity.ok(couponService.createCoupon(request));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<CouponResponse>> findCoupons(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(couponService.findCoupons(restaurantId));
    }

    @PostMapping("/assign")
    public ResponseEntity<CouponResponse> assignCoupon(@RequestBody CouponAssignRequest request) {
        return ResponseEntity.ok(couponService.assignCoupon(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CouponResponse>> findUserCoupons(@PathVariable Long userId) {
        return ResponseEntity.ok(couponService.findUserCoupons(userId));
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.noContent().build();
    }




}
