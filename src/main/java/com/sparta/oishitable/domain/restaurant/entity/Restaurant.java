package com.sparta.oishitable.domain.restaurant.entity;

import com.sparta.oishitable.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "restaurants")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private LocalTime breakStartTime;

    @Column(nullable = false)
    private LocalTime breakEndTime;

    @Column(nullable = false)
    private String introduce;

    @Column(nullable = false)
    private Integer deposit;

    @Column(nullable = false)
    private LocalTime reservationInterval;

    @Builder
    public Restaurant(
            Long id,
            String name,
            String location,
            LocalTime openTime,
            LocalTime closeTime,
            LocalTime breakStartTime,
            LocalTime breakEndTime,
            String introduce,
            Integer deposit,
            LocalTime reservationInterval
    ) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.breakStartTime = breakStartTime;
        this.breakEndTime = breakEndTime;
        this.introduce = introduce;
        this.deposit = deposit;
        this.reservationInterval = reservationInterval;
    }

    public void updateProfile(String name, String introduce, Integer deposit) {
        this.name = name;
        this.introduce = introduce;
        this.deposit = deposit;
    }
}
