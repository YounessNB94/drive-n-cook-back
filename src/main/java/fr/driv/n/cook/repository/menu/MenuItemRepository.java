package fr.driv.n.cook.repository.menu;

import fr.driv.n.cook.repository.menu.entity.MenuItemEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class MenuItemRepository implements PanacheRepositoryBase<MenuItemEntity, Long> {

    public List<MenuItemEntity> listByMenu(Long menuId) {
        return list("menu.id", menuId);
    }
}
