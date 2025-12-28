package fr.driv.n.cook.repository.appointment;

import fr.driv.n.cook.repository.appointment.entity.AppointmentEntity;
import fr.driv.n.cook.shared.AppointmentStatus;
import fr.driv.n.cook.shared.AppointmentType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AppointmentRepository implements PanacheRepositoryBase<AppointmentEntity, Long> {

    public List<AppointmentEntity> listForWarehouse(Long warehouseId) {
        return list("warehouse.id", warehouseId);
    }

    public List<AppointmentEntity> listByTypeAndStatus(AppointmentType type, AppointmentStatus status) {
        return list("type = ?1 and status = ?2", type, status);
    }
}
