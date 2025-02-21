package com.sparta.oishitable.notification.entity;

import com.sparta.oishitable.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "notifications")
@NoArgsConstructor
@DynamicInsert
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String message;

    private String email;

    // 알림 보내야할 시간
    @Column(nullable = false)
    private LocalDateTime scheduledTime;

    @ColumnDefault(value = "false")
    private boolean isSent;

    @Builder
    public Notification(Long userId, String message, String email, LocalDateTime scheduledTime) {
        this.userId = userId;
        this.message = message;
        this.email = email;
        this.scheduledTime = scheduledTime;
    }

    public void updateSent() {
        this.isSent = true;
    }
}
