package com.sparta.oishitable.domain.owner.restaurant.menus.service;

import com.sparta.oishitable.domain.auth.service.AuthService;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.menus.dto.request.MenuCreateRequest;
import com.sparta.oishitable.domain.owner.restaurant.menus.dto.request.MenuUpdateRequest;
import com.sparta.oishitable.domain.owner.restaurant.menus.dto.response.MenuDetailResponse;
import com.sparta.oishitable.domain.owner.restaurant.menus.dto.response.MenuFindResponse;
import com.sparta.oishitable.domain.owner.restaurant.menus.entity.Menu;
import com.sparta.oishitable.domain.owner.restaurant.menus.repository.MenuRepository;
import com.sparta.oishitable.domain.owner.restaurant.service.OwnerRestaurantService;
import com.sparta.oishitable.global.exception.NotFoundException;
import com.sparta.oishitable.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final AuthService authService;
    private final OwnerRestaurantService restaurantService;
    private final MenuRepository menuRepository;

    @Transactional
    public void createMenu(Long userId, Long restaurantId, List<MenuCreateRequest> request) {
        Restaurant restaurant = restaurantService.findById(restaurantId);

        authService.checkUserAuthority(restaurant.getOwner().getId(), userId);

        List<Menu> menus = request.stream()
                .map(m -> m.toEntity(restaurant))
                .toList();

        menuRepository.saveAll(menus);
    }

    @Transactional(readOnly = true)
    public MenuFindResponse findMenus(Long restaurantId) {
        List<Menu> menus = menuRepository.findAllByRestaurantId(restaurantId);

        return MenuFindResponse.from(menus);
    }

    @Transactional(readOnly = true)
    public MenuDetailResponse findMenu(Long restaurantId, Long menuId) {
        Menu menu = findMenuByMenuIdAndRestaurantId(menuId, restaurantId);

        return MenuDetailResponse.from(menu);
    }

    @Transactional
    public void updateMenu(Long userId, Long restaurantId, Long menuId, MenuUpdateRequest request) {
        Menu menu = findMenuByMenuIdAndRestaurantId(menuId, restaurantId);

        authService.checkUserAuthority(menu.getRestaurant().getOwner().getId(), userId);

        menu.update(request.menuName(), menu.getPrice(), menu.getDescription());
    }

    @Transactional
    public void deleteMenu(Long userId, Long restaurantId, Long menuId) {
        Menu menu = findMenuByMenuIdAndRestaurantId(menuId, restaurantId);

        authService.checkUserAuthority(menu.getRestaurant().getOwner().getId(), userId);

        menuRepository.delete(menu);
    }

    private Menu findMenuByMenuIdAndRestaurantId(Long restaurantId, Long menuId) {
        return menuRepository.findByIdAndRestaurantId(menuId, restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MENU_NOT_FOUND));
    }
}
