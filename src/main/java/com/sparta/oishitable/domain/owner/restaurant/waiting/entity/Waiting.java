package com.sparta.oishitable.domain.owner.restaurant.waiting.entity;

import com.sparta.oishitable.domain.common.BaseEntity;
import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.customer.reservation.entity.ReservationStatus;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Entity
@Table(
        name = "waitings",
        indexes = {
                @Index(name = "idx_fk_user_id", columnList = "user_id"),
                @Index(name = "idx_fk_restaurant_id", columnList = "restaurant_id")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Waiting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waiting_id")
    private Long id;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WaitingType type;

    @Column(nullable = false)
    @ColumnDefault("'RESERVED'")
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Builder
    public Waiting(Long id, Integer totalCount, WaitingType type, ReservationStatus status, User user, Restaurant restaurant) {
        this.id = id;
        this.totalCount = totalCount;
        this.type = type;
        this.status = status;
        this.user = user;
        this.restaurant = restaurant;
    }

    public void updateStatus(ReservationStatus status) {
        this.status = status;
    }
}
