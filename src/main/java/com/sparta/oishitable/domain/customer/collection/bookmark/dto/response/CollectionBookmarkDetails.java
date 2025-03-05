package com.sparta.oishitable.domain.customer.collection.bookmark.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import org.locationtech.jts.geom.Point;

public record CollectionBookmarkDetails(
        Long collectionBookmarkId,
        Long restaurantId,
        String bookmarkMemo,
        String restaurantName,
        String restaurantIntroduction,
        String restaurantAddress,
        Point location
) {

    @QueryProjection
    public CollectionBookmarkDetails {
    }
}
