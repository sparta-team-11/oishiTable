package com.sparta.oishitable.domain.customer.bookmark.entity;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "bookmarks",
        indexes = {
                @Index(name = "idx_fk_user_id", columnList = "user_id"),
                @Index(name = "idx_fk_restaurant_id", columnList = "restaurant_id")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @Column(length = 100)
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Builder
    public Bookmark(Long id, String memo, User user, Restaurant restaurant) {
        this.id = id;
        this.memo = memo;
        this.user = user;
        this.restaurant = restaurant;
    }

    public void updateMemo(String updateMemo) {
        this.memo = updateMemo;
    }
}
