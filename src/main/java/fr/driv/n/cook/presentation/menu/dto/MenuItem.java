package fr.driv.n.cook.presentation.menu.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MenuItem(
        Long id,
        Long menuId,
        @NotBlank @Size(max = 150) String name,
        @NotNull @DecimalMin(value = "0.00", inclusive = false) BigDecimal priceCash,
        Integer pointsPrice,
        @NotNull Boolean available
) {
}
