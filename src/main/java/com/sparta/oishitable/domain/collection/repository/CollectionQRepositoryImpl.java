package com.sparta.oishitable.domain.collection.repository;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.oishitable.domain.collection.dto.response.CollectionInfoResponse;
import com.sparta.oishitable.domain.collection.dto.response.QCollectionInfoResponse;
import com.sparta.oishitable.domain.collection.entity.QCollection;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sparta.oishitable.domain.collection.entity.QCollection.collection;

@RequiredArgsConstructor
public class CollectionQRepositoryImpl implements CollectionQRepository {

    private final JPAQueryFactory queryFactory;

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
