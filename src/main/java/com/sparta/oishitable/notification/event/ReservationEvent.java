package com.sparta.oishitable.notification.event;


import java.io.Serializable;

public record ReservationEvent(

        Long reservationId,
        Long userId

) implements Serializable {

}
