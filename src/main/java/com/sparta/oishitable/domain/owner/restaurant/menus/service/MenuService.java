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

import java.util.IntSummaryStatistics;
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

        calculateRestaurantPriceRange(restaurant, menus);
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
        Restaurant restaurant = menu.getRestaurant();

        authService.checkUserAuthority(restaurant.getOwner().getId(), userId);

        int oldPrice = ceilToNearestTenThousand(menu.getPrice());
        int newPrice = ceilToNearestTenThousand(request.menuPrice());

        menu.update(request.menuName(), request.menuPrice(), request.menuDescription());

        if (restaurant.getMinPrice() == oldPrice || restaurant.getMaxPrice() == oldPrice) {
            calculateRestaurantPriceRange(restaurant, restaurant.getMenus());
        } else {
            restaurant.updatePrice(newPrice, newPrice);
        }
    }

    @Transactional
    public void deleteMenu(Long userId, Long restaurantId, Long menuId) {
        Menu menu = findMenuByMenuIdAndRestaurantId(menuId, restaurantId);
        Restaurant restaurant = menu.getRestaurant();

        authService.checkUserAuthority(restaurant.getOwner().getId(), userId);

        int deletedPrice = ceilToNearestTenThousand(menu.getPrice());

        menuRepository.delete(menu);
        restaurant.removeMenu(menu);

        if (restaurant.getMinPrice() == deletedPrice || restaurant.getMaxPrice() == deletedPrice) {
            calculateRestaurantPriceRange(restaurant, restaurant.getMenus());
        }
    }

    private Menu findMenuByMenuIdAndRestaurantId(Long menuId, Long restaurantId) {
        return menuRepository.findByIdAndRestaurantId(menuId, restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MENU_NOT_FOUND));
    }

    private void calculateRestaurantPriceRange(Restaurant restaurant, List<Menu> menus) {
        // min, max, sum 등 통계 정보를 한번에 연산
        IntSummaryStatistics summaryStatistics = menus.stream()
                .mapToInt(Menu::getPrice)
                .summaryStatistics();

        updateRestaurantPrice(restaurant, summaryStatistics.getMin(), summaryStatistics.getMax());
    }

    private void updateRestaurantPrice(Restaurant restaurant, int min, int max) {
        int minPrice = ceilToNearestTenThousand(min);
        int maxPrice = ceilToNearestTenThousand(max);

        restaurant.updatePrice(minPrice, maxPrice);
    }

    private int ceilToNearestTenThousand(int price) {
        return (int) Math.ceil(price / 10000.0) * 10000;
    }
}
