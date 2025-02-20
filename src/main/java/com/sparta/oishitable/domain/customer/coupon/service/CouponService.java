package com.sparta.oishitable.domain.customer.coupon.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
//import com.sparta.oishitable.domain.customer.coupon.dto.CouponAssignRequest;
import com.sparta.oishitable.domain.customer.coupon.dto.CouponCreateRequest;
import com.sparta.oishitable.domain.customer.coupon.dto.CouponResponse;
import com.sparta.oishitable.domain.customer.coupon.entity.Coupon;
import com.sparta.oishitable.domain.customer.coupon.entity.UserCoupon;
import com.sparta.oishitable.domain.customer.coupon.repository.CouponRepository;
import com.sparta.oishitable.domain.customer.coupon.repository.UserCouponRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.repository.RestaurantRepository;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.UnauthorizedException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import com.sparta.oishitable.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public CouponResponse createCoupon(
            Long userId,
            Long restaurantId,
            CouponCreateRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESTAURANT_NOT_FOUND));

        // 인증된 사용자 ID와 요청된 userId가 동일한지 확인
        if (!userId.equals(request.userId())) {
            throw new CustomRuntimeException(ErrorCode.AUTHORIZATION_EXCEPTION);
        }

        Coupon createCoupon = Coupon.builder()
                .discount(request.discount())
                .couponUsed(false)
                .restaurant(restaurant)
//                .user(user)
                .build();

        Coupon savedCoupon = couponRepository.save(createCoupon);

        UserCoupon userCoupon = UserCoupon.builder()
                .user(user)
                .coupon(savedCoupon)
                .build();

        userCouponRepository.save(userCoupon);

        return CouponResponse.from(savedCoupon);

    }

    public List<CouponResponse> findCoupons(Long restaurantId) {
        List<Coupon> coupons = couponRepository.findByRestaurantId(restaurantId);
        return coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }

/*    @Transactional
    public CouponResponse assignCoupon(
            Long userId,
            CouponAssignRequest request
    ) {
        Coupon coupon = couponRepository.findByIdAndCouponUsedFalse(request.couponId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.COUPON_NOT_FOUND));

        // 인증된 사용자 ID와 요청된 userId가 동일한지 확인
        if (!userId.equals(request.userId())) {
            throw new CustomRuntimeException(ErrorCode.AUTHORIZATION_EXCEPTION);
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));


        coupon.assignUser(user);

        return CouponResponse.from(coupon);

    }*/

    @Transactional
    public void deleteCoupon(
            Long couponId
    ) {
        if (!couponRepository.existsById(couponId)) {
            throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
        }
        couponRepository.deleteById(couponId);
    }

    public List<CouponResponse> findUserCoupons(Long userId) {
        List<UserCoupon> usercoupons = userCouponRepository.findByUserId(userId);
        return usercoupons.stream()
                .map(userCoupon -> CouponResponse.from(userCoupon.getCoupon()))
                .collect(Collectors.toList());
    }
}
