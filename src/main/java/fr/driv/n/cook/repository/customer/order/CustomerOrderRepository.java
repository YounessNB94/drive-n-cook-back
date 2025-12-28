package fr.driv.n.cook.repository.customer.order;

import fr.driv.n.cook.repository.customer.order.entity.CustomerOrderEntity;
import fr.driv.n.cook.shared.CustomerOrderStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CustomerOrderRepository implements PanacheRepositoryBase<CustomerOrderEntity, Long> {

    public List<CustomerOrderEntity> listByFranchisee(Long franchiseeId) {
        return list("franchisee.id", franchiseeId);
    }

    public List<CustomerOrderEntity> listByStatus(CustomerOrderStatus status) {
        return list("status", status);
    }
}
