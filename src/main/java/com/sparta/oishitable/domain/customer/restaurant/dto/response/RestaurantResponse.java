package com.sparta.oishitable.domain.customer.restaurant.dto.response;

import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalTime;

@Builder(access = AccessLevel.PRIVATE)
public record RestaurantResponse(
        Long restaurantId,
        String name,
        String location,
        LocalTime openTime,
        LocalTime closeTime,
        LocalTime breakTimeStart,
        LocalTime breakTimeEnd,
        String introduce,
        Integer deposit,
        LocalTime reservationInterval
) {

    public static RestaurantResponse from(Restaurant restaurant) {
        return RestaurantResponse.builder()
                .restaurantId(restaurant.getId())
                .name(restaurant.getName())
                .location(restaurant.getLocation())
                .openTime(restaurant.getOpenTime())
                .closeTime(restaurant.getCloseTime())
                .breakTimeStart(restaurant.getBreakTimeStart())
                .breakTimeEnd(restaurant.getBreakTimeEnd())
                .introduce(restaurant.getIntroduce())
                .deposit(restaurant.getDeposit())
                .reservationInterval(restaurant.getReservationInterval())
                .build();
    }
}
