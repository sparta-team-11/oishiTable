package com.sparta.oishitable.domain.common.notification.repository;

import com.sparta.oishitable.domain.common.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> , NotificationRepositoryQuerydsl{


}
