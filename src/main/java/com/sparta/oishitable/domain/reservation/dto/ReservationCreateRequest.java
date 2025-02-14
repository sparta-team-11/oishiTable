package com.sparta.oishitable.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationCreateRequest(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull
        LocalDateTime date,
        @NotNull
        Integer totalCount,
        @NotNull
        String seatTypeName
) {}

