package fr.driv.n.cook.repository.revenue;

import fr.driv.n.cook.repository.revenue.entity.RevenuePointEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class RevenuePointRepository implements PanacheRepositoryBase<RevenuePointEntity, Long> {

    public List<RevenuePointEntity> listByFranchiseeAndPeriod(Long franchiseeId, LocalDate from, LocalDate to) {
        return list("franchisee.id = ?1 and periodStart >= ?2 and periodEnd <= ?3", franchiseeId, from, to);
    }
}
