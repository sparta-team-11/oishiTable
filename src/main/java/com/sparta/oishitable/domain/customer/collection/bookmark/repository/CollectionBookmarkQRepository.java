package com.sparta.oishitable.domain.customer.collection.bookmark.repository;

import com.sparta.oishitable.domain.customer.collection.bookmark.dto.response.CollectionBookmarkDetails;
import com.sparta.oishitable.domain.customer.collection.bookmark.entity.CollectionBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CollectionBookmarkQRepository {

    Page<CollectionBookmarkDetails> findBookmarkDetailsByCollectionId(Long collectionId, Pageable pageable);
}
