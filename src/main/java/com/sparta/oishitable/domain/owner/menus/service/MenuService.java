package com.sparta.oishitable.domain.owner.menus.service;

import com.sparta.oishitable.domain.auth.service.AuthService;
import com.sparta.oishitable.domain.owner.menus.dto.request.MenuCreateRequest;
import com.sparta.oishitable.domain.owner.menus.dto.response.MenuFindResponse;
import com.sparta.oishitable.domain.owner.menus.entity.Menu;
import com.sparta.oishitable.domain.owner.menus.repository.MenuRepository;
import com.sparta.oishitable.domain.owner.restaurant.entity.Restaurant;
import com.sparta.oishitable.domain.owner.restaurant.service.OwnerRestaurantService;
import jakarta.validation.Valid;
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
    public void createMenu(Long userId, Long restaurantId, List<@Valid MenuCreateRequest> request) {
        Restaurant restaurant = restaurantService.findById(restaurantId);

        authService.checkAuthenticationUser(restaurant.getOwner().getId(), userId);

        List<Menu> menus = request.stream()
                .map(m -> m.toEntity(restaurant))
                .toList();

        menuRepository.saveAll(menus);
    }

    public MenuFindResponse findMenus(Long restaurantId) {
        List<Menu> menus = menuRepository.findAllByRestaurantId(restaurantId);

        return MenuFindResponse.from(menus);
    }
}
