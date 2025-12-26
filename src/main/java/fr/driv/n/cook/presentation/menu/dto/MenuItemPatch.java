package fr.driv.n.cook.presentation.menu.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MenuItemPatch(
        @Size(max = 150) String name,
        @DecimalMin(value = "0.00", inclusive = false) BigDecimal priceCash,
        Integer pointsPrice,
        Boolean available
) {
}
