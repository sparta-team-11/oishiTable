package com.sparta.oishitable.domain.customer.collection.bookmark.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CollectionBookmarkDetails {

    private final Long bookmarkId;
    private final Long restaurantId;
    private final String bookmarkMemo;
    private final String restaurantName;
    private final String restaurantIntroduction;
    private final String restaurantAddress;
    private final Double longitude;
    private final Double latitude;

    @QueryProjection
    public CollectionBookmarkDetails(Long bookmarkId, Long restaurantId, String bookmarkMemo, String restaurantName, String restaurantIntroduction, String restaurantAddress, Double longitude, Double latitude) {
        this.bookmarkId = bookmarkId;
        this.restaurantId = restaurantId;
        this.bookmarkMemo = bookmarkMemo;
        this.restaurantName = restaurantName;
        this.restaurantIntroduction = restaurantIntroduction;
        this.restaurantAddress = restaurantAddress;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
