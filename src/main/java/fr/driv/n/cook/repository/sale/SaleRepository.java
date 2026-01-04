package fr.driv.n.cook.repository.sale;

import fr.driv.n.cook.repository.sale.entity.SaleEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SaleRepository implements PanacheRepositoryBase<SaleEntity, Long> {

    public List<SaleEntity> listSales(Long franchiseeId, LocalDateTime from, LocalDateTime to) {
        if (franchiseeId != null) {
            return find("customerOrder.franchisee.id = ?1",
                    franchiseeId)
                    .list();
        }
        return findAll().list();
    }
}