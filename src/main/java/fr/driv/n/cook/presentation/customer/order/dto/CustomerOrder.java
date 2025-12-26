package fr.driv.n.cook.presentation.customer.order.dto;

import fr.driv.n.cook.shared.CustomerOrderStatus;
import fr.driv.n.cook.shared.CustomerOrderType;
import fr.driv.n.cook.shared.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerOrder(
        Long id,
        @NotNull CustomerOrderType type,
        CustomerOrderStatus status,
        Long loyaltyCardId,
        boolean paid,
        PaymentMethod paymentMethod,
        BigDecimal totalCash,
        Integer totalPoints,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
