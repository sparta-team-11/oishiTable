package com.sparta.oishitable.domain.owner.menus.repository;

import com.sparta.oishitable.domain.owner.menus.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
