package com.sparta.oishitable.notification.repository;

import com.sparta.oishitable.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {


    // 알림 미전송 된 것 중 보내야 하는 알림 조회
    @Query("SELECT n FROM Notification n WHERE n.isSent = false AND n.scheduledTime <= :currentTime")
    List<Notification> findDueNotifications(@Param("currentTime") LocalDateTime currentTime);
}
