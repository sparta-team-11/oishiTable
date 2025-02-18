package com.sparta.oishitable.domain.customer.collection.repository;

import com.sparta.oishitable.domain.customer.collection.dto.response.CollectionDetailResponse;
import com.sparta.oishitable.domain.customer.collection.dto.response.CollectionInfoResponse;

import java.util.List;
import java.util.Optional;

public interface CollectionQRepository {

    Optional<CollectionDetailResponse> findCollectionDetail(Long collectionId);

    List<CollectionInfoResponse> findAllByUserId(Long userId);
}
