package fr.driv.n.cook.repository.incident;

import fr.driv.n.cook.repository.incident.entity.IncidentEntity;
import fr.driv.n.cook.shared.IncidentStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class IncidentRepository implements PanacheRepositoryBase<IncidentEntity, Long> {

    public List<IncidentEntity> listByTruck(Long truckId) {
        return list("truck.id", truckId);
    }

    public List<IncidentEntity> listByStatus(IncidentStatus status) {
        return list("status", status);
    }
}
