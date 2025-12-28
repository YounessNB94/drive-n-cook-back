package fr.driv.n.cook.repository.report;

import fr.driv.n.cook.repository.report.entity.ReportEntity;
import fr.driv.n.cook.shared.ReportStatus;
import fr.driv.n.cook.shared.ReportType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class ReportRepository implements PanacheRepositoryBase<ReportEntity, Long> {

    public List<ReportEntity> listByFranchisee(Long franchiseeId) {
        return list("franchisee.id", franchiseeId);
    }

    public List<ReportEntity> listByTypeAndPeriod(ReportType type, LocalDate from, LocalDate to) {
        return list("type = ?1 and dateFrom >= ?2 and dateTo <= ?3", type, from, to);
    }

    public List<ReportEntity> listByStatus(ReportStatus status) {
        return list("status", status);
    }
}
