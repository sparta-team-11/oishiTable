package com.sparta.oishitable.domain.customer.bookmark.error;

import lombok.Getter;

@Getter
public class BookmarkErrorMessages {
    public static final String RESTAURANT_ID_SHOULD_NOT_BE_NULL = "RestaurantId 값이 NULL 입니다.";
    public static final String MEMO_SHOULD_NOT_BE_NULL = "updateMemo 값이 NULL 입니다.";
}
