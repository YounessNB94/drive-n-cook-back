package fr.driv.n.cook.presentation.truck.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.driv.n.cook.shared.TruckStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TruckPatch(
        Long assignedFranchiseeId,
        Long currentWarehouseId,
        TruckStatus status
) {
}

