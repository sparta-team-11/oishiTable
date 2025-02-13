package com.sparta.oishitable.domain.restaurantseat.dto.request;

import com.sparta.oishitable.domain.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.restaurantseat.entity.RestaurantSeat;
import com.sparta.oishitable.domain.seatType.entity.SeatType;
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
