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

        // 메일 발신자, 수신자, 제목 , 내용 설정
        message.setFrom(SENDER);
        message.setTo(notification.getEmail());
        message.setSubject("오이시 테이블 예약 알림");
        message.setText(notification.getMessage());
        mailSender.send(message);

        notification.updateSent();
        // 영속성 컨텍스트에 관리 안되므로 save 로 변경사항 저장
        notificationRepository.save(notification);
    }
}
