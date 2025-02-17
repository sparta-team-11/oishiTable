package com.sparta.oishitable.domain.customer.restaurant.dto.response;

import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record RestaurantListResponse(
        Long restaurantId,
        String name,
        String location
) {

    public static RestaurantListResponse from(Restaurant restaurant) {
        return RestaurantListResponse.builder()
                .restaurantId(restaurant.getId())
                .name(restaurant.getName())
                .location(restaurant.getLocation())
                .build();
    }
}
