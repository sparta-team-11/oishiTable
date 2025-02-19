package com.sparta.oishitable.domain.customer.restaurant.dto.response;

import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record NearbyRestaurantResponse(
        Long id,
        String name,
        String address,
        Double latitude,
        Double longitude,
        Double distance
) {

    public static NearbyRestaurantResponse from(Restaurant restaurant, Double distance) {
        return NearbyRestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .distance(distance)
                .build();
    }
}