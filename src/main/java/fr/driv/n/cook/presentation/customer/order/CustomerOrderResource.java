package fr.driv.n.cook.presentation.customer.order;

import fr.driv.n.cook.presentation.customer.order.dto.CustomerOrder;
import fr.driv.n.cook.presentation.customer.order.dto.CustomerOrderItem;
import fr.driv.n.cook.presentation.customer.order.dto.CustomerOrderPatch;
import fr.driv.n.cook.presentation.shared.dto.CustomerOrderStatus;
import fr.driv.n.cook.presentation.shared.dto.CustomerOrderType;
import fr.driv.n.cook.presentation.shared.dto.PaymentMethod;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Path("/customer-orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CustomerOrderResource {

    @POST
    public CustomerOrder createCustomerOrder(@Valid CustomerOrder order) {
        return stubOrder(1L, order.type());
    }

    @POST
    @Path("/{orderId}/items")
    public CustomerOrderItem addItem(
            @PathParam("orderId") Long orderId,
            @Valid CustomerOrderItem item
    ) {
        return stubItem(orderId, 1L);
    }

    @PATCH
    @Path("/{orderId}")
    public CustomerOrder updateCustomerOrder(
            @PathParam("orderId") Long orderId,
            @Valid CustomerOrderPatch patch
    ) {
        return stubOrder(orderId, CustomerOrderType.ON_SITE);
    }

    private CustomerOrder stubOrder(Long id, CustomerOrderType type) {
        return new CustomerOrder(
                id,
                type,
                CustomerOrderStatus.PREPARING,
                42L,
                true,
                PaymentMethod.CASH,
                new BigDecimal("32.50"),
                325,
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now()
        );
    }

    private CustomerOrderItem stubItem(Long orderId, Long itemId) {
        return new CustomerOrderItem(
                itemId,
                orderId,
                10L,
                2,
                new BigDecimal("15.00"),
                150
        );
    }
}
