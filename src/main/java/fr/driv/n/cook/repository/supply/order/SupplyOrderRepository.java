package fr.driv.n.cook.repository.supply.order;

import fr.driv.n.cook.repository.supply.order.entity.SupplyOrderEntity;
import fr.driv.n.cook.shared.SupplyOrderStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class SupplyOrderRepository implements PanacheRepositoryBase<SupplyOrderEntity, Long> {

    public List<SupplyOrderEntity> listByFranchisee(Long franchiseeId) {
        return list("franchisee.id", franchiseeId);
    }

    public List<SupplyOrderEntity> listByStatus(SupplyOrderStatus status) {
        return list("status", status);
    }
}
