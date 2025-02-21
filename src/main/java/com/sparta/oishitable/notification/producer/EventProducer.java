//package com.sparta.oishitable.notification.producer;
//
//import com.sparta.oishitable.global.config.RabbitMQConfig;
//import com.sparta.oishitable.notification.event.ReservationEvent;
//import lombok.RequiredArgsConstructor;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class EventProducer {
//
//    private final RabbitTemplate rabbitTemplate;
//
//    public void sendReservationEvent(ReservationEvent event) {
//        rabbitTemplate.convertAndSend(
//                RabbitMQConfig.EXCHANGE,
//                RabbitMQConfig.ROUTING_KEY,
//                event
//        );
//    }
//}
//TODO : RabbitMQ 로컬설치 or 도커로 사용
