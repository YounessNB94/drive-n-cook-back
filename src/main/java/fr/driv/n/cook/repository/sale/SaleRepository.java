package fr.driv.n.cook.repository.sale;

import fr.driv.n.cook.repository.sale.entity.SaleEntity;
import fr.driv.n.cook.shared.SaleChannel;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SaleRepository implements PanacheRepositoryBase<SaleEntity, Long> {

    public List<SaleEntity> listByCustomerOrder(Long orderId) {
        return list("customerOrder.id", orderId);
    }

    public List<SaleEntity> listByChannelBetween(SaleChannel channel, LocalDateTime from, LocalDateTime to) {
        return list("channel = ?1 and date between ?2 and ?3", channel, from, to);
    }
}
