package fr.driv.n.cook.presentation.truck.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record MaintenanceRecord(
        Long id,
        Long truckId,
        @NotNull LocalDateTime date,
        @NotBlank @Size(max = 500) String description
) {
}
