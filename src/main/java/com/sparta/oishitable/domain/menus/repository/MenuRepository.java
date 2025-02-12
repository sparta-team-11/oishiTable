package com.sparta.oishitable.domain.menus.repository;

import com.sparta.oishitable.domain.menus.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
