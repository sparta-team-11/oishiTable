package com.sparta.oishitable.domain.customer.collection.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CollectionDetailResponse {

    private final Long userId;
    private final String name;
    private final String description;
    private final boolean isPublic;
    private final LocalDateTime modifiedAt;

    @QueryProjection
    public CollectionDetailResponse(Long userId, String name, String description, boolean isPublic, LocalDateTime modifiedAt) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.modifiedAt = modifiedAt;
    }
}
