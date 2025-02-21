package com.sparta.oishitable.notification.repository;

import com.sparta.oishitable.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> , NotificationRepositoryQuerydsl{


}
