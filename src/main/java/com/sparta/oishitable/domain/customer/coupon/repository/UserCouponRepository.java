package com.sparta.oishitable.domain.customer.coupon.repository;

import com.sparta.oishitable.domain.customer.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long>, UserCouponRepositoryQuerydsl {

    List<UserCoupon> findByUserId(Long userId);

    long countByCouponId(Long couponId);

    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);
}
