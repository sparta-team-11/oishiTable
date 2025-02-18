package com.sparta.oishitable.domain.owner.restaurant.dto.response;

import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record RestaurantFindResponse(
        Long restaurantId,
        Long ownerId,
        String name,
        String location,
        LocalTime openTime,
        LocalTime closeTime,
        LocalTime breakTimeStart,
        LocalTime breakTimeEnd,
        String introduce,
        int deposit,
        LocalTime reservationInterval
) {

    public static RestaurantFindResponse from(Restaurant restaurant) {
        return RestaurantFindResponse.builder()
                .restaurantId(restaurant.getId())
                .ownerId(restaurant.getOwner().getId())
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
