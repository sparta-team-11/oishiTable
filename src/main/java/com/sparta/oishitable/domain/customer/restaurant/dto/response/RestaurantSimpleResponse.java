package com.sparta.oishitable.domain.customer.restaurant.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class RestaurantSimpleResponse {
    private final Long restaurantId;
    private final String restaurantName;
    private final String location;

    @QueryProjection
    public RestaurantSimpleResponse(
            Long restaurantId,
            String restaurantName,
            String location
    ) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.location = location;
    }
}
