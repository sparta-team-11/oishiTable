package com.sparta.oishitable.domain.common.notification.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.customer.reservation.entity.Reservation;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.global.exception.CustomRuntimeException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import com.sparta.oishitable.domain.common.notification.entity.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value(value = "${spring.mail.username}")
    private String sender;

    public void sendEmail(Notification notification) {

        Reservation reservation = notification.getReservation();
        User user = notification.getUser();
        String mailContent = createMailContent(notification, reservation, user);

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(user.getEmail());
            helper.setSubject("오이시 테이블 예약 알림");
            helper.setText(mailContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new CustomRuntimeException(ErrorCode.EMAIL_SENDING_FAILED);
        }
    }

    private static String createMailContent(Notification notification, Reservation reservation, User user) {
        Restaurant restaurant = reservation.getRestaurantSeat().getRestaurant();

        // 공통 예약 정보
        String commonContent = String.format(
                        "<p><strong>예약 시간:</strong> %s</p>" +
                        "<p><strong>식당:</strong> %s (%s)</p>" +
                        "<p><strong>예약자:</strong> %s</p>" +
                        "<p><strong>예약 인원:</strong> %d</p>",
                reservation.getDate(),
                restaurant.getName(),
                restaurant.getAddress(),
                user.getName(),             // TODO : 예약 생성시 예약자명을 받아서 쓰게끔 차후 변경
                reservation.getTotalCount()
        );

        // 이메일 본문 구성
        String mailContent =
                "<html>" +
                "<body>" +
                "<h2 style='color: #2e6c80;'>" + notification.getHeaderMessage() + "</h2>" +
                commonContent +
                "</body>" +
                "</html>";

        return mailContent;
    }
}