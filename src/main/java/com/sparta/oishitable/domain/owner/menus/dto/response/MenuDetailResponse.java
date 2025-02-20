package com.sparta.oishitable.domain.owner.menus.dto.response;

import com.sparta.oishitable.domain.owner.menus.entity.Menu;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MenuDetailResponse(
        Long menuId,
        String menuName,
        Integer menuPrice,
        String menuDescription
) {

    public static MenuDetailResponse from(Menu menu) {
        return MenuDetailResponse.builder()
                .menuId(menu.getId())
                .menuName(menu.getName())
                .menuPrice(menu.getPrice())
                .menuDescription(menu.getDescription())
                .build();
    }
}
