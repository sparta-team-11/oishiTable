package com.sparta.oishitable.domain.customer.restaurant.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class RestaurantSimpleResponse {
    private final Long restaurantId;
    private final String restaurantName;
    private final String address;

    @QueryProjection
    public RestaurantSimpleResponse(
            Long restaurantId,
            String restaurantName,
            String address
    ) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.address = address;
    }
}
