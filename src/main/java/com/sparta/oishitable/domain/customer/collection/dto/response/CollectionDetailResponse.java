package com.sparta.oishitable.domain.customer.collection.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CollectionDetailResponse {

    private final String name;
    private final String description;
    private final LocalDateTime modifiedAt;

    @QueryProjection
    public CollectionDetailResponse(String name, String description, LocalDateTime modifiedAt) {
        this.name = name;
        this.description = description;
        this.modifiedAt = modifiedAt;
    }
}
