package com.sparta.oishitable.domain.restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RestaurantProfileUpdateRequest(
        @NotBlank
        String name,

        @NotBlank
        String introduce,

        @NotNull
        Integer deposit
) {
}
