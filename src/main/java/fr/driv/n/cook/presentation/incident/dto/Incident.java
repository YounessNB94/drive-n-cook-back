package fr.driv.n.cook.presentation.incident.dto;

import fr.driv.n.cook.shared.IncidentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record Incident(
        Long id,
        Long truckId,
        @NotBlank @Size(max = 500) String description,
        IncidentStatus status,
        LocalDateTime createdAt
) {
}
