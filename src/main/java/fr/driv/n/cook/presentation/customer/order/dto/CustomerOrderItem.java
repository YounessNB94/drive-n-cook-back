package fr.driv.n.cook.presentation.customer.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CustomerOrderItem(
        Long id,
        Long customerOrderId,
        @NotNull Long menuItemId,
        @NotNull @Min(1) Integer quantity,
        BigDecimal lineCashTotal,
        Integer linePointsTotal
) {
}
