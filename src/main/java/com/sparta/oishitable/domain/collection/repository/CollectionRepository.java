package com.sparta.oishitable.domain.collection.repository;

import com.sparta.oishitable.domain.collection.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long>, CollectionQRepository {
}
