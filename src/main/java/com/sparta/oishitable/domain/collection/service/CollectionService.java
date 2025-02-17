package com.sparta.oishitable.domain.collection.service;

import com.sparta.oishitable.domain.collection.dto.request.CollectionCreateRequest;
import com.sparta.oishitable.domain.collection.dto.request.CollectionUpdateRequest;
import com.sparta.oishitable.domain.collection.entity.Collection;
import com.sparta.oishitable.domain.collection.repository.CollectionRepository;
import com.sparta.oishitable.domain.user.entity.User;
import com.sparta.oishitable.domain.user.repository.UserRepository;
import com.sparta.oishitable.global.exception.ForbiddenException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createCollection(Long userId, CollectionCreateRequest collectionCreateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Collection collection = Collection.builder()
                .user(user)
                .name(collectionCreateRequest.name())
                .description(collectionCreateRequest.description())
                .isPublic(collectionCreateRequest.isPublic())
                .build();

        collectionRepository.save(collection);
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
