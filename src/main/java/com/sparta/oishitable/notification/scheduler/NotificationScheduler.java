package com.sparta.oishitable.notification.scheduler;

import com.sparta.oishitable.domain.customer.reservation.entity.Reservation;
import com.sparta.oishitable.notification.entity.Notification;
import com.sparta.oishitable.notification.repository.NotificationRepository;
import com.sparta.oishitable.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 */5 * * * ?")
    public void processDueNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<Notification> dueNotifications = notificationRepository.findDueNotifications(now);
        for (Notification notification : dueNotifications) {
            notificationService.sendEmail(notification);
        }
    }
}
