package com.sparta.oishitable.domain.restaurant.dto.response;

import com.sparta.oishitable.domain.restaurant.entity.Restaurant;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record RestaurantResponse (
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
            .breakTimeStart(restaurant.getBreakStartTime())
            .breakTimeEnd(restaurant.getBreakEndTime())
            .introduce(restaurant.getIntroduce())
            .deposit(restaurant.getDeposit())
            .reservationInterval(restaurant.getReservationInterval())
            .build();
    }
}
