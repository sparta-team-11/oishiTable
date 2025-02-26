package com.sparta.oishitable.domain.customer.collection.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CollectionInfoResponse {

    private final Long collectionId;
    private final String name;
    private final boolean isPublic;

    @QueryProjection
    public CollectionInfoResponse(Long collectionId, String name, boolean isPublic) {
        this.collectionId = collectionId;
        this.name = name;
        this.isPublic = isPublic;
    }
}
