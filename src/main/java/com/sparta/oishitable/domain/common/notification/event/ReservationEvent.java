package com.sparta.oishitable.domain.common.notification.event;


import java.io.Serializable;

public record ReservationEvent(

        Long reservationId,
        Long userId

) implements Serializable {

}
