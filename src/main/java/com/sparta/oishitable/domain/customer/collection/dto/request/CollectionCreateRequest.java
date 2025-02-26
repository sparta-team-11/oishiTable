package com.sparta.oishitable.domain.customer.collection.dto.request;

import com.sparta.oishitable.domain.customer.collection.error.CollectionErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CollectionCreateRequest(
        @NotBlank(message = CollectionErrorMessage.NAME_SHOULD_NOT_BE_NULL_OR_EMPTY)
        String name,

        @NotNull(message = CollectionErrorMessage.DESCRIPTION_SHOULD_NOT_BE_NULL)
        String description,

        @NotNull(message = CollectionErrorMessage.IS_PUBLIC_SHOULD_NOT_BE_NULL)
        Boolean isPublic
) {
}
