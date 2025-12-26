package fr.driv.n.cook.presentation.franchise.application.dto;

import jakarta.validation.constraints.Size;

public record FranchiseApplicationRequest(
        @Size(max = 1000) String note
) {
}
