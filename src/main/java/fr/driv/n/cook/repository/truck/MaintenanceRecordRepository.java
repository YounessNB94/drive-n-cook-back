package fr.driv.n.cook.repository.truck;

import fr.driv.n.cook.repository.truck.entity.MaintenanceRecordEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class MaintenanceRecordRepository implements PanacheRepositoryBase<MaintenanceRecordEntity, Long> {

    public List<MaintenanceRecordEntity> listByTruck(Long truckId) {
        return list("truck.id", truckId);
    }
}
