package fr.driv.n.cook.presentation.franchise.application.dto;

import fr.driv.n.cook.presentation.shared.dto.PaymentMethod;
import jakarta.validation.constraints.Size;

public record FranchiseApplicationPatch(
        Boolean paid,
        PaymentMethod paymentMethod,
        @Size(max = 100) String paymentRef
) {
}
