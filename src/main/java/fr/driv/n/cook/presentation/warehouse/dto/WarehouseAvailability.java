package fr.driv.n.cook.presentation.warehouse.dto;

import java.util.List;

public record WarehouseAvailability(
        Long warehouseId,
        String warehouseName,
        boolean sufficient,
        List<ItemAvailability> items
) {

    public record ItemAvailability(
            Long inventoryItemId,
            String name,
            Integer requestedQuantity,
            Integer availableQuantity
    ) {
    }
}
