package com.sparta.oishitable.domain.customer.coupon.entity;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne // Restaurant 엔티티와 관계를 설정
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant; // restaurant 필드 추가

    @Column(name = "discount")
    private Integer discount = 10;

    @Column(name = "coupon_exist")
    private Boolean couponExist = false;

    @Column(name = "coupon_provided")
    private Integer couponProvided = 100;

    @Builder
    public Coupon(
            Long id,
            User user,
            Integer discount,
            Boolean couponExist,
            Integer couponProvided

            ) {
        this.id = id;
        this.user = user;
        this.discount = discount;
        this.couponExist = couponExist;
        this.couponProvided = couponProvided;
    }

    public Integer getDiscount() {
        return this.discount;
    }

    public boolean getCouponExist() {
        return this.couponExist;
    }

//    public void discountCoupon(Integer discount){
//        this.discount =  discount;
//        this.couponExist = true;
//    }

    public Long getId(){
        return this.id;
    }

    public Integer getCouponProvided() {
        return this.couponProvided;
    }

    public void setCouponProvided(Integer couponProvided){
        this.couponProvided = couponProvided;
    }

    public void setCouponExist(Boolean couponExist) {
        this.couponExist = couponExist;
    }
}

