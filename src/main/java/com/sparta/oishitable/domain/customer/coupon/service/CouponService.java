package com.sparta.oishitable.domain.customer.coupon.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.coupon.dto.CouponAssignRequest;
import com.sparta.oishitable.domain.customer.coupon.dto.CouponCreateRequest;
import com.sparta.oishitable.domain.customer.coupon.dto.CouponResponse;
import com.sparta.oishitable.domain.customer.coupon.entity.Coupon;
import com.sparta.oishitable.domain.customer.coupon.repository.CouponRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.repository.RestaurantRepository;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private CouponRepository couponRepository;
    private RestaurantRepository restaurantRepository;
    private UserRepository userRepository;

    @Transactional
    public CouponResponse createCoupon(CouponCreateRequest request) {
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESTAURANT_NOT_FOUND));

        Coupon createCoupon = Coupon.builder()
                .restaurant(restaurant)
                .discount(request.discount())
                .couponUsed(false)
                .build();

        Coupon savedCoupon = couponRepository.save(createCoupon);

        return new CouponResponse(
                savedCoupon.getId(),
                savedCoupon.getDiscount(),
                savedCoupon.getCouponUsed(),
                savedCoupon.getRestaurant().getId(),
                savedCoupon.getUser() != null ? savedCoupon.getUser().getId() : null
        );
    }

    public List<CouponResponse> findCoupons(Long restaurantId) {
        List<Coupon> coupons = couponRepository.findByRestaurantId(restaurantId);
        return coupons.stream()
                .map(coupon -> new CouponResponse(
                        coupon.getId(),
                        coupon.getDiscount(),
                        coupon.getCouponUsed(),
                        coupon.getRestaurant().getId(),
                        coupon.getUser() != null ? coupon.getUser().getId() : null
                ))
                .collect(Collectors.toList());


    }

    @Transactional
    public CouponResponse assignCoupon(CouponAssignRequest request) {
        Coupon coupon = couponRepository.findByIdAndCouponUsedFalse(request.couponId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.COUPON_NOT_FOUND));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (coupon.getUser() != null) {
            throw new CustomRuntimeException(ErrorCode.COUPON_ALREADY_ASSIGNED);
        }

        coupon.assignUser(user);

        return new CouponResponse(
                coupon.getId(),
                coupon.getDiscount(),
                coupon.getCouponUsed(),
                coupon.getRestaurant().getId(),
                coupon.getUser() != null ? coupon.getUser().getId() : null
        );
    }

    public List<CouponResponse> findUserCoupons(Long userId) {
        List<Coupon> coupons = couponRepository.findByUserId(userId);
        return coupons.stream()
                .map(coupon -> new CouponResponse(
                        coupon.getId(),
                        coupon.getDiscount(),
                        coupon.getCouponUsed(),
                        coupon.getRestaurant().getId(),
                        coupon.getUser() != null ? coupon.getUser().getId() : null
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCoupon(Long couponId) {
        if (!couponRepository.existsById(couponId)) {
            throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
        }
        couponRepository.deleteById(couponId);
    }


}
