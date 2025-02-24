package com.sparta.oishitable.domain.customer.coupon.repository;

import com.sparta.oishitable.domain.customer.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    List<UserCoupon> findByUserId(Long userId);

    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

    Optional<UserCoupon> findByIdAndCouponUsedFalse(Long couponId);
}
