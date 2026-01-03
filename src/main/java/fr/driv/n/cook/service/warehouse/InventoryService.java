package fr.driv.n.cook.service.warehouse;

import fr.driv.n.cook.presentation.warehouse.dto.InventoryItem;
import fr.driv.n.cook.presentation.warehouse.dto.InventoryItemPatch;
import fr.driv.n.cook.repository.warehouse.InventoryItemRepository;
import fr.driv.n.cook.repository.warehouse.WarehouseRepository;
import fr.driv.n.cook.repository.warehouse.entity.InventoryItemEntity;
import fr.driv.n.cook.repository.warehouse.entity.WarehouseEntity;
import fr.driv.n.cook.service.warehouse.mapper.InventoryItemMapper;
import fr.driv.n.cook.service.warehouse.mapper.WarehouseMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class InventoryService {

    @Inject
    InventoryItemRepository inventoryItemRepository;

    @Inject
    WarehouseRepository warehouseRepository;

    @Inject
    InventoryItemMapper inventoryItemMapper;

    @Inject
    WarehouseMapper warehouseMapper;

    public List<InventoryItem> listInventoryItems(Long warehouseId) {
        WarehouseEntity warehouse = warehouseRepository.findByIdOptional(warehouseId)
                .orElseThrow(() -> new NotFoundException("Entrepôt introuvable"));

        return inventoryItemRepository.list("warehouse", warehouse).stream()
                .map(inventoryItemMapper::toDto)
                .toList();
    }

    @Transactional
    public InventoryItem createInventoryItem(Long warehouseId, InventoryItem item) {
        WarehouseEntity warehouse = fetchWarehouse(warehouseId);
        ensureUniqueName(warehouseId, item.name());
        InventoryItemEntity entity = inventoryItemMapper.toEntity(item);
        entity.setWarehouse(warehouse);
        inventoryItemRepository.persist(entity);
        return inventoryItemMapper.toDto(entity);
    }

    @Transactional
    public InventoryItem patchInventoryItem(Long warehouseId, Long itemId, InventoryItemPatch patch) {
        WarehouseEntity warehouse = fetchWarehouse(warehouseId);
        InventoryItemEntity entity = inventoryItemRepository.findByIdOptional(itemId)
                .orElseThrow(() -> new NotFoundException("Article introuvable"));
        if (!entity.getWarehouse().getId().equals(warehouse.getId())) {
            throw new BadRequestException("L'article n'appartient pas à cet entrepôt");
        }
        inventoryItemMapper.updateEntityFromPatch(patch, entity);
        return inventoryItemMapper.toDto(entity);
    }

    private WarehouseEntity fetchWarehouse(Long warehouseId) {
        return warehouseRepository.findByIdOptional(warehouseId)
                .orElseThrow(() -> new NotFoundException("Entrepôt introuvable"));
    }

    private void ensureUniqueName(Long warehouseId, String name) {
        if (inventoryItemRepository.existsByWarehouseAndName(warehouseId, name)) {
            throw new BadRequestException("Cet article existe déjà dans cet entrepôt");
        }
    }
}
