package fr.driv.n.cook.repository.incident;

import fr.driv.n.cook.repository.incident.entity.IncidentEntity;
import fr.driv.n.cook.shared.IncidentStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class IncidentRepository implements PanacheRepositoryBase<IncidentEntity, Long> {

    public List<IncidentEntity> listByFilters(IncidentStatus status, Long truckId, Long franchiseeId) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();
        if (status != null) {
            query.append(" and status = :status");
            params.put("status", status);
        }
        if (truckId != null) {
            query.append(" and truck.id = :truckId");
            params.put("truckId", truckId);
        }
        if (franchiseeId != null) {
            query.append(" and truck.franchisee.id = :franchiseeId");
            params.put("franchiseeId", franchiseeId);
        }
        return list(query.toString(), params);
    }
}
