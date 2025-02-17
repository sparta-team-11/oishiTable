package com.sparta.oishitable.domain.customer.collection.dto.request;


public record CollectionUpdateRequest(
        String name,
        String description,
        Boolean isPublic
) {
}
