package fr.driv.n.cook.repository.warehouse;

import fr.driv.n.cook.repository.warehouse.entity.InventoryItemEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class InventoryItemRepository implements PanacheRepositoryBase<InventoryItemEntity, Long> {

    public List<InventoryItemEntity> listByWarehouse(Long warehouseId) {
        return list("warehouse.id", warehouseId);
    }

    public boolean existsByWarehouseAndName(Long warehouseId, String name) {
        return count("warehouse.id = ?1 and LOWER(name) = ?2", warehouseId, name.toLowerCase()) > 0;
    }
}
