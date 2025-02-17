package com.sparta.oishitable.domain.owner.restaurantseat.dto.request;

import com.sparta.oishitable.domain.admin.seatType.entity.SeatType;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurantseat.entity.RestaurantSeat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RestaurantSeatCreateRequest(
        @NotBlank
        Long seatTypeId,

        @NotNull
        Integer minGuestCount,

        @NotNull
        Integer maxGuestCount,

        @NotNull
        Integer quantity
) {

    public RestaurantSeat toEntity(Restaurant restaurant, SeatType seatType) {
        return RestaurantSeat.builder()
                .seatType(seatType)
                .restaurant(restaurant)
                .minGuestCount(minGuestCount)
                .maxGuestCount(maxGuestCount)
                .quantity(quantity)
                .build();
    }
}
