package com.sparta.oishitable.domain.customer.coupon.controller;

//import com.sparta.oishitable.domain.customer.coupon.dto.CouponAssignRequest;
import com.sparta.oishitable.domain.customer.coupon.dto.CouponCreateRequest;
import com.sparta.oishitable.domain.customer.coupon.dto.CouponResponse;
import com.sparta.oishitable.domain.customer.coupon.service.CouponService;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import com.sparta.oishitable.global.util.UriBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/owner/api/restaurants/{restaurantId}/coupons")
@RequiredArgsConstructor
public class OwnerCouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(
            @PathVariable Long restaurantId,
            @RequestBody CouponCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        CouponResponse response = couponService.createCoupon(userDetails.getId(), restaurantId, request);
        URI location = UriBuilderUtil.create("/owner/api/restaurants/{restaurantId}", response.restaurantId());

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> findCoupons(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(couponService.findCoupons(restaurantId));
    }

//    @PostMapping("/{couponId}/assign")
//    public ResponseEntity<CouponResponse> assignCoupon(
//            @PathVariable Long restaurantId,
//            @PathVariable Long couponId,
//            @RequestBody CouponAssignRequest request,
//            @AuthenticationPrincipal CustomUserDetails userDetails
//    ) {
//        return ResponseEntity.ok(couponService.assignCoupon(userDetails.getId(), request));
//    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(
            @PathVariable Long restaurantId,
            @PathVariable Long couponId
    ) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.noContent().build();
    }

}
