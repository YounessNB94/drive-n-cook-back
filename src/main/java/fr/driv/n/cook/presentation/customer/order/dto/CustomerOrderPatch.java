package fr.driv.n.cook.presentation.customer.order.dto;

import fr.driv.n.cook.shared.CustomerOrderStatus;
import fr.driv.n.cook.shared.PaymentMethod;

public record CustomerOrderPatch(
        CustomerOrderStatus status,
        Boolean paid,
        PaymentMethod paymentMethod
) {
}
