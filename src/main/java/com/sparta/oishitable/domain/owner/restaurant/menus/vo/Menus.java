package com.sparta.oishitable.domain.owner.restaurant.menus.vo;

import com.sparta.oishitable.domain.owner.restaurant.menus.entity.Menu;
import com.sparta.oishitable.global.exception.InvalidException;
import com.sparta.oishitable.global.exception.error.ErrorCode;

import java.util.List;
import java.util.Objects;

public class Menus {

    private final List<Menu> menus;

    public Menus(List<Menu> menus) {
        this.menus = menus;
    }

    public int getMinPrice() {
        if (Objects.isNull(menus) || menus.isEmpty()) {
            throw new InvalidException(ErrorCode.MENUS_EMPTY);
        }

        return this.menus.stream()
                .mapToInt(Menu::getPrice)
                .min()
                .getAsInt();
    }

    public int getMaxPrice() {
        if (Objects.isNull(menus) || menus.isEmpty()) {
            throw new InvalidException(ErrorCode.MENUS_EMPTY);
        }

        return this.menus.stream()
                .mapToInt(Menu::getPrice)
                .max()
                .getAsInt();
    }
}
