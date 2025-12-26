package fr.driv.n.cook.presentation.supply.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SupplyOrderItemPatch(
        @NotNull @Min(1) Integer quantity
) {
}
