package com.sparta.oishitable.domain.bookmark.entity;

import com.sparta.oishitable.domain.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.user.entity.User;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Builder
    public Bookmark(Long id, User user, Restaurant restaurant) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
    }
}
