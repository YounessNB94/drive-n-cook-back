package fr.driv.n.cook.repository.appointment;

import fr.driv.n.cook.repository.appointment.entity.AppointmentEntity;
import fr.driv.n.cook.shared.AppointmentStatus;
import fr.driv.n.cook.shared.AppointmentType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class AppointmentRepository implements PanacheRepositoryBase<AppointmentEntity, Long> {

    public List<AppointmentEntity> listForWarehouse(Long warehouseId) {
        return list("warehouse.id", warehouseId);
    }

    public List<AppointmentEntity> listByTypeAndStatus(AppointmentType type, AppointmentStatus status) {
        return list("type = ?1 and status = ?2", type, status);
    }

    public List<AppointmentEntity> listByFilters(AppointmentType type, Long warehouseId, LocalDateTime from, LocalDateTime to) {
        return listWithFilters("1=1", new HashMap<>(), type, warehouseId, from, to);
    }

    public List<AppointmentEntity> listByFranchisee(Long franchiseeId) {
        return list("franchisee.id", franchiseeId);
    }

    public List<AppointmentEntity> listByFranchisee(Long franchiseeId, AppointmentType type, Long warehouseId, LocalDateTime from, LocalDateTime to) {
        Map<String, Object> params = new HashMap<>();
        params.put("franchiseeId", franchiseeId);
        return listWithFilters("franchisee.id = :franchiseeId", params, type, warehouseId, from, to);
    }

    private List<AppointmentEntity> listWithFilters(String baseCondition,
                                                    Map<String, Object> params,
                                                    AppointmentType type,
                                                    Long warehouseId,
                                                    LocalDateTime from,
                                                    LocalDateTime to) {
        StringBuilder query = new StringBuilder(baseCondition);
        if (type != null) {
            query.append(" and type = :type");
            params.put("type", type);
        }
        if (warehouseId != null) {
            query.append(" and warehouse.id = :warehouseId");
            params.put("warehouseId", warehouseId);
        }
        if (from != null) {
            query.append(" and datetime >= :from");
            params.put("from", from);
        }
        if (to != null) {
            query.append(" and datetime <= :to");
            params.put("to", to);
        }
        return list(query.toString(), params);
    }
}
