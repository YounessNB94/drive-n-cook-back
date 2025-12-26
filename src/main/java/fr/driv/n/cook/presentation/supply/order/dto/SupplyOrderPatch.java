package fr.driv.n.cook.presentation.supply.order.dto;

import fr.driv.n.cook.presentation.shared.dto.PaymentMethod;
import fr.driv.n.cook.presentation.shared.dto.SupplyOrderStatus;
import jakarta.validation.constraints.Size;

public record SupplyOrderPatch(
        Long pickupWarehouseId,
        SupplyOrderStatus status,
        Boolean paid,
        PaymentMethod paymentMethod,
        @Size(max = 100) String paymentRef
) {
}
