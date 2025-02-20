package com.sparta.oishitable.domain.customer.coupon.service;

import com.sparta.oishitable.domain.customer.coupon.controller.UserCouponController;
import com.sparta.oishitable.domain.customer.coupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;
}
