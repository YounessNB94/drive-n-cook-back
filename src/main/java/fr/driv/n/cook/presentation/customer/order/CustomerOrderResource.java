package fr.driv.n.cook.presentation.customer.order;

import fr.driv.n.cook.presentation.customer.order.dto.CustomerOrder;
import fr.driv.n.cook.presentation.customer.order.dto.CustomerOrderItem;
import fr.driv.n.cook.presentation.customer.order.dto.CustomerOrderPatch;
import fr.driv.n.cook.service.customer.order.CustomerOrderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/customer-orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CustomerOrderResource {

    @Inject
    CustomerOrderService customerOrderService;

    @POST
    public CustomerOrder createCustomerOrder(@Valid CustomerOrder order) {
        return customerOrderService.createOrder(currentFranchiseeId(), order);
    }

    @POST
    @Path("/{orderId}/items")
    public CustomerOrderItem addItem(
            @PathParam("orderId") Long orderId,
            @Valid CustomerOrderItem item
    ) {
        return customerOrderService.addItem(orderId, item);
    }

    @PATCH
    @Path("/{orderId}")
    public CustomerOrder updateCustomerOrder(
            @PathParam("orderId") Long orderId,
            @Valid CustomerOrderPatch patch
    ) {
        return customerOrderService.patchOrder(orderId, patch);
    }

    private Long currentFranchiseeId() {
        return 1L;
    }
}
