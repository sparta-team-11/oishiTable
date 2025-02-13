package com.sparta.oishitable.domain.reservation.dto;

import com.sparta.oishitable.domain.restaurantseat.entity.RestaurantSeat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

public record UserCreateRequest(
        @NotNull LocalDateTime date,
        @NotNull Integer totalCount,
        @NotNull String seatTypeName
) {}

