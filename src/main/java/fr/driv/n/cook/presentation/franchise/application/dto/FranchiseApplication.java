package fr.driv.n.cook.presentation.franchise.application.dto;

import fr.driv.n.cook.shared.FranchiseApplicationStatus;
import fr.driv.n.cook.shared.PaymentMethod;

import java.time.LocalDateTime;

public record FranchiseApplication(
        Long id,
        FranchiseApplicationStatus status,
        boolean paid,
        PaymentMethod paymentMethod,
        String paymentRef,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
