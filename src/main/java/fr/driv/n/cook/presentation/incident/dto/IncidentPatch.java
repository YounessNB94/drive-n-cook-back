package fr.driv.n.cook.presentation.incident.dto;

import fr.driv.n.cook.presentation.shared.dto.IncidentStatus;
import jakarta.validation.constraints.Size;

public record IncidentPatch(
        @Size(max = 500) String description,
        IncidentStatus status
) {
}
