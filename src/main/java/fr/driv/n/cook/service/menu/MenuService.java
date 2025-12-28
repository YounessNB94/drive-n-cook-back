package fr.driv.n.cook.service.menu;

import fr.driv.n.cook.presentation.menu.dto.Menu;
import fr.driv.n.cook.presentation.menu.dto.MenuItem;
import fr.driv.n.cook.presentation.menu.dto.MenuItemPatch;
import fr.driv.n.cook.presentation.menu.dto.MenuPatch;
import fr.driv.n.cook.repository.franchisee.FranchiseeRepository;
import fr.driv.n.cook.repository.franchisee.entity.FranchiseeEntity;
import fr.driv.n.cook.repository.menu.MenuItemRepository;
import fr.driv.n.cook.repository.menu.MenuRepository;
import fr.driv.n.cook.repository.menu.entity.MenuEntity;
import fr.driv.n.cook.repository.menu.entity.MenuItemEntity;
import fr.driv.n.cook.service.menu.mapper.MenuMapper;
import fr.driv.n.cook.shared.MenuStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class MenuService {

    @Inject
    MenuRepository menuRepository;

    @Inject
    MenuItemRepository menuItemRepository;

    @Inject
    FranchiseeRepository franchiseeRepository;

    @Inject
    MenuMapper mapper;

    @Transactional
    public Menu createMenu(Long franchiseeId) {
        FranchiseeEntity franchisee = fetchFranchisee(franchiseeId);
        MenuEntity entity = new MenuEntity();
        entity.setFranchisee(franchisee);
        entity.setStatus(MenuStatus.DRAFT);
        menuRepository.persist(entity);
        return mapper.toDto(entity);
    }

    public Menu getMenu(Long menuId) {
        return mapper.toDto(fetchMenu(menuId));
    }

    public List<Menu> listMenusForFranchisee(Long franchiseeId) {
        return menuRepository.listByFranchisee(franchiseeId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public Menu patchMenu(Long menuId, MenuPatch patch) {
        MenuEntity entity = fetchMenu(menuId);
        mapper.updateEntityFromPatch(patch, entity);
        return mapper.toDto(entity);
    }

    public List<MenuItem> listItems(Long menuId) {
        return menuItemRepository.listByMenu(menuId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public MenuItem createItem(Long menuId, MenuItem item) {
        MenuEntity menu = fetchMenu(menuId);
        validateItemPayload(item.priceCash(), item.pointsPrice());

        MenuItemEntity entity = new MenuItemEntity();
        entity.setMenu(menu);
        entity.setName(item.name());
        entity.setPriceCash(item.priceCash());
        entity.setPointsPrice(item.pointsPrice());
        entity.setAvailable(item.available());

        menuItemRepository.persist(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public MenuItem patchItem(Long menuItemId, MenuItemPatch patch) {
        MenuItemEntity entity = fetchMenuItem(menuItemId);
        if (patch.priceCash() != null) {
            validatePrice(patch.priceCash());
        }
        if (patch.pointsPrice() != null && patch.pointsPrice() <= 0) {
            throw new BadRequestException("pointsPrice doit être positif");
        }
        mapper.updateEntityFromPatch(patch, entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public void deleteItem(Long menuItemId) {
        MenuItemEntity entity = fetchMenuItem(menuItemId);
        menuItemRepository.delete(entity);
    }

    private void validateItemPayload(BigDecimal priceCash, Integer pointsPrice) {
        validatePrice(priceCash);
        if (pointsPrice != null && pointsPrice <= 0) {
            throw new BadRequestException("pointsPrice doit être positif");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("priceCash doit être positif");
        }
    }

    private FranchiseeEntity fetchFranchisee(Long franchiseeId) {
        return franchiseeRepository.findByIdOptional(franchiseeId)
                .orElseThrow(() -> new NotFoundException("Franchisé introuvable"));
    }

    private MenuEntity fetchMenu(Long menuId) {
        return menuRepository.findByIdOptional(menuId)
                .orElseThrow(() -> new NotFoundException("Menu introuvable"));
    }

    private MenuItemEntity fetchMenuItem(Long menuItemId) {
        return menuItemRepository.findByIdOptional(menuItemId)
                .orElseThrow(() -> new NotFoundException("Item introuvable"));
    }
}
