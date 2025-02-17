package com.sparta.oishitable.domain.collection.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CollectionInfoResponse {

    private final String name;
    private final boolean isPublic;

    @QueryProjection
    public CollectionInfoResponse(String name, boolean isPublic) {
        this.name = name;
        this.isPublic = isPublic;
    }
}
