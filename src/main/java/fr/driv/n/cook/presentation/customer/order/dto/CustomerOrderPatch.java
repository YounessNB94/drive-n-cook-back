package fr.driv.n.cook.presentation.customer.order.dto;

import fr.driv.n.cook.presentation.shared.dto.CustomerOrderStatus;
import fr.driv.n.cook.presentation.shared.dto.PaymentMethod;

public record CustomerOrderPatch(
        CustomerOrderStatus status,
        Boolean paid,
        PaymentMethod paymentMethod
) {
}
