package com.sparta.oishitable.domain.customer.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.oishitable.domain.customer.reservation.entity.Reservation;
import com.sparta.oishitable.domain.customer.reservation.entity.ReservationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record ReservationFindResponse(
        @NotNull
        Long reservationId,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull
        LocalDateTime date,

        @NotNull
        Integer totalCount,

        @NotNull
        ReservationStatus status,

        @NotNull
        Long restaurantId,

        @NotBlank
        String restaurantName,

        @NotBlank
        String restaurantLocation,

        @NotNull
        Long seatTypeId,

        @NotBlank
        String seatTypeName
) {

    public static ReservationFindResponse from(Reservation reservation) {
        return ReservationFindResponse.builder()
                .reservationId(reservation.getId())
                .date(reservation.getDate())
                .totalCount(reservation.getTotalCount())
                .status(reservation.getStatus())
                .restaurantId(reservation.getRestaurantSeat().getRestaurant().getId())
                .restaurantName(reservation.getRestaurantSeat().getRestaurant().getName())
                .restaurantLocation(reservation.getRestaurantSeat().getRestaurant().getLocation())
                .seatTypeId(reservation.getRestaurantSeat().getSeatType().getId())
                .seatTypeName(reservation.getRestaurantSeat().getSeatType().getName())
                .build();
    }
}
