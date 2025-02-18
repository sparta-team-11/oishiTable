package com.sparta.oishitable.domain.customer.collection.bookmark.service;

import com.sparta.oishitable.domain.customer.bookmark.entity.Bookmark;
import com.sparta.oishitable.domain.customer.bookmark.repository.BookmarkRepository;
import com.sparta.oishitable.domain.customer.collection.bookmark.dto.request.BookmarkIdRequest;
import com.sparta.oishitable.domain.customer.collection.bookmark.dto.request.CollectionBookmarkCreateRequest;
import com.sparta.oishitable.domain.customer.collection.bookmark.entity.CollectionBookmark;
import com.sparta.oishitable.domain.customer.collection.bookmark.repository.CollectionBookmarkRepository;
import com.sparta.oishitable.domain.customer.collection.entity.Collection;
import com.sparta.oishitable.domain.customer.collection.repository.CollectionRepository;
import com.sparta.oishitable.global.exception.ConflictException;
import com.sparta.oishitable.global.exception.ForbiddenException;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CollectionBookmarkService {

    private final CollectionBookmarkRepository collectionBookmarkRepository;
    private final CollectionRepository collectionRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public void createCollectionBookmark(Long userId, Long collectionId, Long bookmarkId) {
        if (collectionBookmarkRepository.existsByCollectionIdAndBookmarkId(collectionId, bookmarkId)) {
            throw new ConflictException(ErrorCode.ALREADY_EXISTS_BOOKMARK_IN_COLLECTION);
        }

        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COLLECTION_NOT_FOUND));

        checkUserAuthority(collection.getUser().getId(), userId);

        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOKMARK_NOT_FOUND));

        checkUserAuthority(bookmark.getUser().getId(), userId);

        CollectionBookmark collectionBookmark = CollectionBookmark.builder()
                .collection(collection)
                .bookmark(bookmark)
                .build();

        collectionBookmarkRepository.save(collectionBookmark);
    }

    @Transactional
    public void createCollectionBookmarks(
            Long userId,
            Long collectionId, CollectionBookmarkCreateRequest collectionBookmarkCreateRequest
    ) {
        List<Long> bookmarkIds = collectionBookmarkCreateRequest.bookmarks().stream()
                .map(BookmarkIdRequest::bookmarkId)
                .toList();

        if (collectionBookmarkRepository.existsByCollectionIdAndBookmarkIds(collectionId, bookmarkIds)) {
            throw new ConflictException(ErrorCode.ALREADY_EXISTS_BOOKMARK_IN_COLLECTION);
        }

        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COLLECTION_NOT_FOUND));

        checkUserAuthority(collection.getUser().getId(), userId);

        List<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarkIds(bookmarkIds);

        // 요청한 id 값들로 조회한 bookmark 레코드들이 가져와졌는지 검사
        // 이후 CollectionBookmark 객체 리스트로 mapping
        List<CollectionBookmark> collectionBookmarks = collectionBookmarkCreateRequest.bookmarks().stream()
                .map(collectionBookmark -> {
                    Bookmark findBookmark = bookmarks.stream()
                            .filter(bookmark -> bookmark.getId().equals(collectionBookmark.bookmarkId()))
                            .findFirst()
                            .orElseThrow(() -> new NotFoundException(ErrorCode.BOOKMARK_NOT_FOUND));

                    checkUserAuthority(findBookmark.getUser().getId(), userId);

                    return CollectionBookmark.builder()
                            .collection(collection)
                            .bookmark(findBookmark)
                            .build();
                })
                .toList();

        collectionBookmarkRepository.saveAll(collectionBookmarks);
    }

    @Transactional
    public void deleteCollectionBookmark(Long userId, Long collectionBookmarkId) {
        CollectionBookmark collectionBookmark = collectionBookmarkRepository.findByCollectionBookmarkId(collectionBookmarkId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOKMARK_NOT_FOUND));

        checkUserAuthority(collectionBookmark.getBookmark().getUser().getId(), userId);

        collectionBookmarkRepository.delete(collectionBookmark);
    }

    private void checkUserAuthority(Long recordOwnerId, Long userId) {
        if (!recordOwnerId.equals(userId)) {
            throw new ForbiddenException(ErrorCode.USER_UNAUTHORIZED);
        }
    }
}
