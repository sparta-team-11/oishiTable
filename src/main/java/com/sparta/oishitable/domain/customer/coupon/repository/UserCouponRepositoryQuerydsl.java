package com.sparta.oishitable.domain.customer.coupon.repository;

import com.sparta.oishitable.domain.customer.coupon.entity.UserCoupon;

import java.util.List;

public interface UserCouponRepositoryQuerydsl {
    List<UserCoupon> findByUserIdAndCouponUsedFalseAndIdGreaterThan(Long userId, Long cursor, int size);
}
