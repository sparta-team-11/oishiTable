package com.sparta.oishitable.domain.reservation.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationCreateRequest(
        @NotNull LocalDateTime date,
        @NotNull Integer totalCount,
        @NotNull String seatTypeName
) {}

