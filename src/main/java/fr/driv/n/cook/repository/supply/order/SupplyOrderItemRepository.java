package fr.driv.n.cook.repository.supply.order;

import fr.driv.n.cook.repository.supply.order.entity.SupplyOrderItemEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class SupplyOrderItemRepository implements PanacheRepositoryBase<SupplyOrderItemEntity, Long> {

    public List<SupplyOrderItemEntity> listByOrder(Long orderId) {
        return list("supplyOrder.id", orderId);
    }
}
