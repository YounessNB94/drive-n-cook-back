package fr.driv.n.cook.service.warehouse;

import fr.driv.n.cook.presentation.warehouse.dto.InventoryItem;
import fr.driv.n.cook.repository.warehouse.InventoryItemRepository;
import fr.driv.n.cook.repository.warehouse.WarehouseRepository;
import fr.driv.n.cook.repository.warehouse.entity.WarehouseEntity;
import fr.driv.n.cook.service.warehouse.mapper.WarehouseMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class InventoryService {

    @Inject
    InventoryItemRepository inventoryItemRepository;

    @Inject
    WarehouseRepository warehouseRepository;

    @Inject
    WarehouseMapper mapper;

    public List<InventoryItem> listInventoryItems(Long warehouseId) {
        WarehouseEntity warehouse = warehouseRepository.findByIdOptional(warehouseId)
                .orElseThrow(() -> new NotFoundException("Entrepôt introuvable"));

        return inventoryItemRepository.list("warehouse", warehouse).stream()
                .map(mapper::toDto)
                .toList();
    }
}

