package fr.driv.n.cook.repository.supply.order;

import fr.driv.n.cook.repository.supply.order.entity.SupplyOrderEntity;
import fr.driv.n.cook.shared.SupplyOrderStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class SupplyOrderRepository implements PanacheRepositoryBase<SupplyOrderEntity, Long> {

    public List<SupplyOrderEntity> listByFranchisee(Long franchiseeId) {
        return list("franchisee.id", franchiseeId);
    }

    public List<SupplyOrderEntity> listByStatus(SupplyOrderStatus status) {
        return list("status", status);
    }

    public List<SupplyOrderEntity> listByFilters(SupplyOrderStatus status, Long warehouseId, Long franchiseeId, Boolean paid) {
        StringBuilder jpql = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();
        if (status != null) {
            jpql.append(" and status = :status");
            params.put("status", status);
        }
        if (warehouseId != null) {
            jpql.append(" and pickupWarehouse.id = :warehouseId");
            params.put("warehouseId", warehouseId);
        }
        if (franchiseeId != null) {
            jpql.append(" and franchisee.id = :franchiseeId");
            params.put("franchiseeId", franchiseeId);
        }
        if (paid != null) {
            jpql.append(" and paid = :paid");
            params.put("paid", paid);
        }
        return find(jpql.toString(), params).list();
    }
}
