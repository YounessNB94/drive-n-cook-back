package fr.driv.n.cook.presentation.sale.dto;

import fr.driv.n.cook.shared.SaleChannel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Sale(
        Long id,
        @NotNull LocalDateTime date,
        Long menuItemId,
        @Min(0) int quantity,
        @NotNull BigDecimal totalAmount,
        @NotNull SaleChannel channel
) {
}
