package fr.driv.n.cook.presentation.warehouse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InventoryItem(
        Long id,
        @NotNull Long warehouseId,
        @NotBlank @Size(max = 150) String name,
        @NotBlank @Size(max = 30) String unit,
        int availableQuantity
) {
}
