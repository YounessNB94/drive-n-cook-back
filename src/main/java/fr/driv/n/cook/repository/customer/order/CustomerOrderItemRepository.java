package fr.driv.n.cook.repository.customer.order;

import fr.driv.n.cook.repository.customer.order.entity.CustomerOrderItemEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CustomerOrderItemRepository implements PanacheRepositoryBase<CustomerOrderItemEntity, Long> {

    public List<CustomerOrderItemEntity> listByOrder(Long orderId) {
        return list("customerOrder.id", orderId);
    }
}
