package com.sparta.oishitable.domain.customer.coupon.service;

import com.sparta.oishitable.domain.common.user.repository.UserRepository;
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
public class OwnerCouponService {

    private final CouponRepository couponRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public CouponResponse createCoupon(
            Long userId,
            Long restaurantId,
            CouponCreateRequest request
    ) {
        System.out.println(" 서비스에서 받은 userId " + userId);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESTAURANT_NOT_FOUND));

        System.out.println(" 레스토랑 주인 ID " + restaurant.getOwner().getId());

        // 인증된 사용자 ID와 요청된 userId가 동일한지 확인
        if (!userId.equals(restaurant.getOwner().getId())) {
            throw new CustomRuntimeException(ErrorCode.AUTHORIZATION_EXCEPTION);
        }

        Coupon createCoupon = Coupon.builder()
                .discount(request.discount())
                .restaurant(restaurant)
                .build();

        Coupon savedCoupon = couponRepository.save(createCoupon);


        return CouponResponse.from(savedCoupon);

    }

    public List<CouponResponse> findCoupons(Long restaurantId) {
        List<Coupon> coupons = couponRepository.findByRestaurantId(restaurantId);
        return coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCoupon(
            Long restaurantId,
            Long couponId,
            Long userId
    ) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESTAURANT_NOT_FOUND));

        System.out.println(" 레스토랑 주인 ID: " + restaurant.getOwner().getId());

        // 인증된 사용자 ID와 요청된 userId가 동일한지 확인
        if (!userId.equals(restaurant.getOwner().getId())) {
            throw new CustomRuntimeException(ErrorCode.AUTHORIZATION_EXCEPTION);
        }
        if (!couponRepository.existsById(couponId)) {
            throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
        }
        couponRepository.deleteById(couponId);
    }

}
