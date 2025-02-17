package com.sparta.oishitable.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.oishitable.domain.reservation.entity.ReservationStatus;
import com.sparta.oishitable.domain.restaurantseat.entity.RestaurantSeat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationFindResponse(
    @NotNull
    Long restaurantSeatId,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    LocalDateTime date,
    @NotNull
    Integer totalCount,
    @NotNull
    String seatTypeName,
    @NotNull
    ReservationStatus status,
    @NotNull
    boolean couponExist
) {}
