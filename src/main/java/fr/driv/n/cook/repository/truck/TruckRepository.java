package fr.driv.n.cook.repository.truck;

import fr.driv.n.cook.repository.truck.entity.TruckEntity;
import fr.driv.n.cook.shared.TruckStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TruckRepository implements PanacheRepositoryBase<TruckEntity, Long> {

    public List<TruckEntity> listByFranchisee(Long franchiseeId) {
        return list("franchisee.id", franchiseeId);
    }

    public List<TruckEntity> listByStatus(TruckStatus status) {
        return list("status", status);
    }

    public List<TruckEntity> listByFilters(TruckStatus status, Long warehouseId) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();
        if (status != null) {
            query.append(" and status = :status");
            params.put("status", status);
        }
        if (warehouseId != null) {
            query.append(" and currentWarehouse.id = :warehouseId");
            params.put("warehouseId", warehouseId);
        }
        return find(query.toString(), params).list();
    }
}
