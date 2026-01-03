package fr.driv.n.cook.presentation.warehouse.dto;

import jakarta.validation.constraints.Min;

public record InventoryItemPatch(
        @Min(0) Integer availableQuantity
) {
}

