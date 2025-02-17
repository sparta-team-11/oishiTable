package com.sparta.oishitable.domain.customer.collection.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.customer.collection.dto.response.CollectionDetailResponse;
import com.sparta.oishitable.domain.customer.collection.dto.response.CollectionInfoResponse;
import com.sparta.oishitable.domain.customer.collection.dto.response.QCollectionDetailResponse;
import com.sparta.oishitable.domain.customer.collection.dto.response.QCollectionInfoResponse;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.sparta.oishitable.domain.customer.collection.entity.QCollection.collection;


@RequiredArgsConstructor
public class CollectionQRepositoryImpl implements CollectionQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CollectionDetailResponse> findCollectionDetail(Long collectionId) {
        return Optional.ofNullable(queryFactory
                .select(new QCollectionDetailResponse(
                        collection.name, collection.description, collection.modifiedAt
                ))
                .from(collection)
                .where(collection.id.eq(collectionId))
                .fetchOne());
    }

    @Override
    public List<CollectionInfoResponse> findAllByUserId(Long userId) {
        return queryFactory
                .select(new QCollectionInfoResponse(
                        collection.name, collection.isPublic))
                .from(collection)
                .where(collection.user.id.eq(userId))
                .fetch();
    }
}
