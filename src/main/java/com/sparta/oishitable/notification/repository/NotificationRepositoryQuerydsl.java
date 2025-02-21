package com.sparta.oishitable.notification.repository;

import com.sparta.oishitable.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface NotificationRepositoryQuerydsl {

    Page<Notification> findDueNotifications(LocalDateTime currentTime, Pageable pageable);
    int updateIsSentTrue(Long id);
}
