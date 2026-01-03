package fr.driv.n.cook.presentation.franchise.application.dto;

import fr.driv.n.cook.shared.FranchiseApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record FranchiseApplicationStatusPatch(
        @NotNull FranchiseApplicationStatus status
) {
}

