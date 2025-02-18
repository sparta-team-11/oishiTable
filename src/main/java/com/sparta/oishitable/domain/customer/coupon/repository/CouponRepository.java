package com.sparta.oishitable.domain.customer.coupon.repository;

import com.sparta.oishitable.domain.customer.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon>  findByRestaurantId(Long restaurantId);
}
