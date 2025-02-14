package com.sparta.oishitable.domain.reservation.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReservationStatus {

    COMPLETED, RESERVED, CANCELED;

    @JsonValue
    public String toJson() {
        if (this == RESERVED) {
            return "COMPLETED";
        }
        return this.name();
    }
}
