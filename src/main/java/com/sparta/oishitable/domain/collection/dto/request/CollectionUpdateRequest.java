package com.sparta.oishitable.domain.collection.dto.request;


public record CollectionUpdateRequest(
        String name,
        String description,
        Boolean isPublic
) {
}
