package fr.driv.n.cook.presentation.truck.dto;

import fr.driv.n.cook.shared.TruckStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record Truck(
        Long id,
        @NotBlank @Size(max = 15) String plateNumber,
        @NotNull TruckStatus status,
        Long currentWarehouseId
) {
}
