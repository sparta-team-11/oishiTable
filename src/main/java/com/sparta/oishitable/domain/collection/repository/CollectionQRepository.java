package com.sparta.oishitable.domain.collection.repository;

import com.sparta.oishitable.domain.collection.dto.response.CollectionInfoResponse;

import java.util.List;

public interface CollectionQRepository {

    List<CollectionInfoResponse> findAllByUserId(Long userId);
}
