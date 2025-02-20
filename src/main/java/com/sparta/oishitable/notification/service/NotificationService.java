package com.sparta.oishitable.notification.service;

import com.sparta.oishitable.notification.entity.Notification;
import com.sparta.oishitable.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;
    private final String SENDER = "oishitable52@gmail.com";

    public void sendEmail(Notification notification) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(SENDER);
        message.setTo("gkdl4239@gmail.com"); // 실제 환경에서는 사용자 이메일 주소로 대체
        message.setSubject("오이시 테이블 예약 알림");
        message.setText(notification.getMessage());
        mailSender.send(message);

        notification.updateSent();
        notificationRepository.save(notification);
    }
}
