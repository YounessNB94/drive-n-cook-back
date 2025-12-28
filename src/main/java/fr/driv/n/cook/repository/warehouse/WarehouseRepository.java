package fr.driv.n.cook.repository.warehouse;

import fr.driv.n.cook.repository.warehouse.entity.WarehouseEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WarehouseRepository implements PanacheRepositoryBase<WarehouseEntity, Long> {
}

