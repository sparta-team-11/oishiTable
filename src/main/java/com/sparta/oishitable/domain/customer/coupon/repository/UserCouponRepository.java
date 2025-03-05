package com.sparta.oishitable.domain.customer.coupon.repository;

import com.sparta.oishitable.domain.customer.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long>, UserCouponRepositoryQuerydsl {

    List<UserCoupon> findByUserId(Long userId);

    @Query("SELECT COUNT(uc) FROM UserCoupon uc WHERE uc.coupon.id = :couponId")
    int countDownloadedCoupons(@Param("couponId") Long couponId);

    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);
}
