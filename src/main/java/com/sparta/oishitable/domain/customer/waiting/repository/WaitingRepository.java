package com.sparta.oishitable.domain.customer.waiting.repository;

import com.sparta.oishitable.domain.owner.restaurant.waiting.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingRepository extends JpaRepository<Waiting, Long>, WaitingQueryDslRepository {
}
