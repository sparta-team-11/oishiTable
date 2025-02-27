package com.sparta.oishitable.domain.customer.coupon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.customer.coupon.entity.QUserCoupon;
import com.sparta.oishitable.domain.customer.coupon.entity.UserCoupon;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserCouponRepositoryQuerydslImpl implements UserCouponRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserCoupon> findByUserIdAndCouponUsedFalseAndIdGreaterThan(Long userId, Long cursor, int size) {
        QUserCoupon qUserCoupon = QUserCoupon.userCoupon;

        return queryFactory
                .selectFrom(qUserCoupon)
                .where(
                        qUserCoupon.user.id.eq(userId),
                        qUserCoupon.couponUsed.isFalse(),
                        cursor != null ? qUserCoupon.id.gt(cursor) : null
                )
                .orderBy(qUserCoupon.id.asc())
                .limit(size)
                .fetch();
    }
}
