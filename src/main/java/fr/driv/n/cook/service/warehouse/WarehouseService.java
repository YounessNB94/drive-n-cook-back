package fr.driv.n.cook.service.warehouse;

import fr.driv.n.cook.presentation.warehouse.dto.Warehouse;
import fr.driv.n.cook.repository.warehouse.WarehouseRepository;
import fr.driv.n.cook.repository.warehouse.entity.WarehouseEntity;
import fr.driv.n.cook.service.warehouse.mapper.WarehouseMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class WarehouseService {

    @Inject
    WarehouseRepository warehouseRepository;

    @Inject
    WarehouseMapper mapper;

    public List<Warehouse> listWarehouses() {
        return warehouseRepository.listAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    private WarehouseEntity fetchWarehouse(Long warehouseId) {
        return warehouseRepository.findByIdOptional(warehouseId)
                .orElseThrow(() -> new NotFoundException("Entrepôt introuvable"));
    }
}
