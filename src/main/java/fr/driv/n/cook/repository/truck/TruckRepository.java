package fr.driv.n.cook.repository.truck;

import fr.driv.n.cook.repository.truck.entity.TruckEntity;
import fr.driv.n.cook.shared.TruckStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TruckRepository implements PanacheRepositoryBase<TruckEntity, Long> {

    public List<TruckEntity> listByFranchisee(Long franchiseeId) {
        return list("franchisee.id", franchiseeId);
    }

    public List<TruckEntity> listByStatus(TruckStatus status) {
        return list("status", status);
    }
}
