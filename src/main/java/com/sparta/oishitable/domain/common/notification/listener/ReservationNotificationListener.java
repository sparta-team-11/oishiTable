//package com.sparta.oishitable.notification.listener;
//
//import com.sparta.oishitable.notification.event.ReservationEvent;
//import com.sparta.oishitable.notification.service.NotificationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class ReservationNotificationListener {
//
//    private final NotificationService notificationService;
//
//    @RabbitListener(queues = "reservationQueue")
//    public void handleReservationNotification(ReservationEvent event) {
//        notificationService.createReservationNotification(event);
//    }
//
//}
//TODO : RabbitMQ 로컬설치 or 도커로 사용