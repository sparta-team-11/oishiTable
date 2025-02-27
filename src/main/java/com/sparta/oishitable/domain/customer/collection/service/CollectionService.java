package com.sparta.oishitable.domain.customer.collection.service;

import com.sparta.oishitable.domain.common.user.entity.User;
import com.sparta.oishitable.domain.common.user.repository.UserRepository;
import com.sparta.oishitable.domain.customer.collection.dto.request.CollectionCreateRequest;
import com.sparta.oishitable.domain.customer.collection.dto.request.CollectionUpdateRequest;
import com.sparta.oishitable.domain.customer.collection.dto.response.CollectionDetailResponse;
import com.sparta.oishitable.domain.customer.collection.dto.response.CollectionInfoResponse;
import com.sparta.oishitable.domain.customer.collection.dto.response.CollectionInfosResponse;
import com.sparta.oishitable.domain.customer.collection.entity.Collection;
import com.sparta.oishitable.domain.customer.collection.repository.CollectionRepository;
import com.sparta.oishitable.global.exception.ForbiddenException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createCollection(Long userId, CollectionCreateRequest collectionCreateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Collection collection = Collection.builder()
                .user(user)
                .name(collectionCreateRequest.name())
                .description(collectionCreateRequest.description())
                .isPublic(collectionCreateRequest.isPublic())
                .build();

        Collection savedCollection = collectionRepository.save(collection);

        return savedCollection.getId();
    }

    public CollectionDetailResponse findCollection(Long userId, Long collectionId) {
        CollectionDetailResponse collectionDetailResponse = collectionRepository.findCollectionDetail(collectionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COLLECTION_NOT_FOUND));

        if (!collectionDetailResponse.userId().equals(userId) && !collectionDetailResponse.isPublic()) {
            throw new ForbiddenException(ErrorCode.USER_UNAUTHORIZED);
        }

        return collectionDetailResponse;
    }

    public CollectionInfosResponse findCollections(Long userId, Long collectionOwnerId, Pageable pageable) {
        Page<CollectionInfoResponse> collectionInfos = null;

        if (userId.equals(collectionOwnerId)) {
            collectionInfos = collectionRepository.findAllByCollectionOwnerId(collectionOwnerId, pageable);
        }

        if (!userId.equals(collectionOwnerId)) {
            collectionInfos = collectionRepository.findAllByPublicCollections(collectionOwnerId, pageable);
        }

        return CollectionInfosResponse.from(collectionInfos);
    }

    @Transactional
    public void updateCollection(Long userId, Long collectionId, CollectionUpdateRequest collectionUpdateRequest) {
        Collection collection = findById(collectionId);

        if (!collection.getUser().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.USER_UNAUTHORIZED);
        }

        collection.updateCollectionInfo(collectionUpdateRequest);
    }

    @Transactional
    public void deleteCollection(Long userId, Long collectionId) {
        Collection collection = findById(collectionId);

        if (!collection.getUser().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.USER_UNAUTHORIZED);
        }

        collectionRepository.delete(collection);
    }

    private Collection findById(Long collectionId) {
        return collectionRepository.findById(collectionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COLLECTION_NOT_FOUND));
    }
}
