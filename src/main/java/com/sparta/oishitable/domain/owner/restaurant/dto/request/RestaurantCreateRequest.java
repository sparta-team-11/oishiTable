package com.sparta.oishitable.domain.owner.restaurant.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurantseat.dto.request.RestaurantSeatCreateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;

public record RestaurantCreateRequest(
        @NotNull
        Long userId,

        @NotBlank
        String name,

        @NotBlank
        String location,

        @NotBlank
        @JsonFormat(pattern = "HH:mm")
        LocalTime openTime,

        @NotBlank
        @JsonFormat(pattern = "HH:mm")
        LocalTime closeTime,

        @NotBlank
        @JsonFormat(pattern = "HH:mm")
        LocalTime breakTimeStart,

        @NotBlank
        @JsonFormat(pattern = "HH:mm")
        LocalTime breakTimeEnd,

        @NotBlank
        String introduce,

        @NotNull
        Integer deposit,

        @NotBlank
        @JsonFormat(pattern = "HH:mm")
        LocalTime reservationInterval,

        List<RestaurantSeatCreateRequest> restaurantSeatCreateRequestList
) {

    public Restaurant toEntity(User owner) {
        return Restaurant.builder()
                .name(name)
                .location(location)
                .openTime(openTime)
                .closeTime(closeTime)
                .breakTimeStart(breakTimeStart)
                .breakTimeEnd(breakTimeEnd)
                .introduce(introduce)
                .deposit(deposit)
                .reservationInterval(reservationInterval)
                .owner(owner)
                .build();
    }
}
