package com.sparta.oishitable.domain.owner.restaurant.entity;

import com.sparta.oishitable.domain.common.BaseEntity;
import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.owner.restaurant.menus.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "restaurants",
        indexes = {
                @Index(name = "idx_fk_owner_id", columnList = "owner_id")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private LocalTime breakTimeStart;

    @Column(nullable = false)
    private LocalTime breakTimeEnd;

    @Column(nullable = false)
    private String introduce;

    @Column(nullable = false)
    private int deposit;

    @Column(nullable = false)
    private LocalTime reservationInterval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private Integer minPrice;
    private Integer maxPrice;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @Builder
    public Restaurant(
            String name,
            String location,
            LocalTime openTime,
            LocalTime closeTime,
            LocalTime breakTimeStart,
            LocalTime breakTimeEnd,
            String introduce,
            int deposit,
            LocalTime reservationInterval,
            User owner,
            String address,
            Double latitude,
            Double longitude,
            Integer minPrice,
            Integer maxPrice
    ) {
        this.name = name;
        this.location = location;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.breakTimeStart = breakTimeStart;
        this.breakTimeEnd = breakTimeEnd;
        this.introduce = introduce;
        this.deposit = deposit;
        this.reservationInterval = reservationInterval;
        this.owner = owner;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public void updateProfile(String name, String introduce, Integer deposit) {
        this.name = name;
        this.introduce = introduce;
        this.deposit = deposit;
    }

    public void updatePrice(int minPrice, int maxPrice) {
        updateMinPrice(minPrice);
        updateMaxPrice(maxPrice);
    }

    public void addMenus(List<Menu> menus) {
        this.menus.addAll(menus);
    }

    public void removeMenu(Menu menu) {
        this.menus.remove(menu);
    }

    private void updateMinPrice(int minPrice) {
        if (this.minPrice > minPrice) {
            this.minPrice = minPrice;
        }
    }

    private void updateMaxPrice(int maxPrice) {
        if (this.maxPrice < maxPrice) {
            this.maxPrice = maxPrice;
        }
    }

    public void initializePrice() {
        this.maxPrice = 0;
        this.minPrice = Integer.MAX_VALUE;
    }
}
