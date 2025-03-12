package com.sparta.oishitable.domain.owner.coupon.service;

import com.sparta.oishitable.domain.common.auth.service.AuthService;
import com.sparta.oishitable.domain.owner.coupon.dto.request.CouponCreateRequest;
import com.sparta.oishitable.domain.owner.coupon.dto.request.CouponResponse;
import com.sparta.oishitable.domain.owner.coupon.entity.Coupon;
import com.sparta.oishitable.domain.owner.coupon.entity.CouponType;
import com.sparta.oishitable.domain.owner.coupon.repository.CouponRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.service.OwnerRestaurantService;
import com.sparta.oishitable.global.aop.annotation.DistributedLock;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OwnerCouponService {

    private final CouponRepository couponRepository;
    private final OwnerRestaurantService ownerRestaurantService;
    private final AuthService authService;

    @DistributedLock(key = "'coupon:' + #restaurantId")
    public CouponResponse createCoupon(
            Long userId,
            Long restaurantId,
            CouponCreateRequest request
    ) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);

        authService.checkUserAuthority(restaurant.getOwner().getId(), userId);

        //(null or 0) 일때 FIRST_COME 이 생성 안되도록 한다.
        if (request.type() == CouponType.FIRST_COME && (request.firstComeCouponMaxCount() == null || request.firstComeCouponMaxCount() <= 0)) {
            throw new CustomRuntimeException(ErrorCode.COUPON_REQUEST_INVALID);
        }

        if (request.type() == CouponType.GENERAL && request.firstComeCouponMaxCount() != null && request.firstComeCouponMaxCount() >= 1) {
            throw new CustomRuntimeException(ErrorCode.COUPON_GENERAL_REQUEST_INVALID);
        }


        Coupon createCoupon = Coupon.builder()
                .couponName(request.couponName())
                .discount(request.discount())
                .firstComeCouponMaxCount(request.firstComeCouponMaxCount() != null ? request.firstComeCouponMaxCount() : 0)
                .restaurant(restaurant)
                .type(request.type())
                .build();

        Coupon savedCoupon = couponRepository.save(createCoupon);


        return CouponResponse.from(savedCoupon);

    }

    public List<CouponResponse> findCoupons(Long restaurantId) {
        List<Coupon> coupons = couponRepository.findByRestaurantId(restaurantId);
        return coupons.stream()
                .map(CouponResponse::from)
                .toList();
    }

    @Transactional
    public void deleteCoupon(
            Long restaurantId,
            Long couponId,
            Long userId
    ) {
        Restaurant restaurant = ownerRestaurantService.findById(restaurantId);

        authService.checkUserAuthority(restaurant.getOwner().getId(), userId);

        if (!couponRepository.existsById(couponId)) {
            throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
        }
        couponRepository.deleteById(couponId);
    }
}
