package fr.driv.n.cook.presentation.supply.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SupplyOrderItem(
        Long id,
        Long supplyOrderId,
        @NotNull Long inventoryItemId,
        @NotNull @Min(1) Integer quantity
) {
}
