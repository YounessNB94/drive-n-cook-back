package fr.driv.n.cook.repository.menu;

import fr.driv.n.cook.repository.menu.entity.MenuEntity;
import fr.driv.n.cook.shared.MenuStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class MenuRepository implements PanacheRepositoryBase<MenuEntity, Long> {

    public List<MenuEntity> listByFranchisee(Long franchiseeId) {
        return list("franchisee.id", franchiseeId);
    }

    public List<MenuEntity> listByStatus(MenuStatus status) {
        return list("status", status);
    }
}
