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
        @NotBlank
        String name,

        @NotBlank
        String address,

        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime openTime,

        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime closeTime,

        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime breakTimeStart,

        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime breakTimeEnd,

        @NotBlank
        String introduce,

        @NotNull
        Integer deposit,

        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime reservationInterval,

        List<RestaurantSeatCreateRequest> restaurantSeatCreateRequestList
) {

    public Restaurant toEntity(User owner, double latitude, double longitude) {
        return Restaurant.builder()
                .name(name)
                .address(address)
                .openTime(openTime)
                .closeTime(closeTime)
                .breakTimeStart(breakTimeStart)
                .breakTimeEnd(breakTimeEnd)
                .introduce(introduce)
                .deposit(deposit)
                .reservationInterval(reservationInterval)
                .latitude(latitude)
                .longitude(longitude)
                .owner(owner)
                .build();
    }
}
