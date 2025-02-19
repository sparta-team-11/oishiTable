package com.sparta.oishitable.domain.customer.bookmark.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class BookmarkDetails {

    private final Long bookmarkId;
    private final Long restaurantId;
    private final String bookmarkMemo;
    private final String restaurantName;
    private final String restaurantIntroduction;
    private final String restaurantLocation;

    @QueryProjection
    public BookmarkDetails(Long bookmarkId, Long restaurantId, String bookmarkMemo, String restaurantName, String restaurantIntroduction, String restaurantLocation) {
        this.bookmarkId = bookmarkId;
        this.restaurantId = restaurantId;
        this.bookmarkMemo = bookmarkMemo;
        this.restaurantName = restaurantName;
        this.restaurantIntroduction = restaurantIntroduction;
        this.restaurantLocation = restaurantLocation;
    }
}
