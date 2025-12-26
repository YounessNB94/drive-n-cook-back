package fr.driv.n.cook.presentation.supply.order.dto;

import fr.driv.n.cook.shared.PaymentMethod;
import fr.driv.n.cook.shared.SupplyOrderStatus;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record SupplyOrder(
        Long id,
        SupplyOrderStatus status,
        Long pickupWarehouseId,
        boolean paid,
        PaymentMethod paymentMethod,
        @Size(max = 100) String paymentRef,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
