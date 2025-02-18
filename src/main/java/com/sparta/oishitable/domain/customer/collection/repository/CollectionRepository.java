package com.sparta.oishitable.domain.customer.collection.repository;

import com.sparta.oishitable.domain.customer.collection.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long>, CollectionQRepository {
}
