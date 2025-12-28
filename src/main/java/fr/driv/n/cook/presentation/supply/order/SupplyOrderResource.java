package fr.driv.n.cook.presentation.supply.order;

import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrder;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderItem;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderItemPatch;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderPatch;
import fr.driv.n.cook.service.supply.order.SupplyOrderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/supply-orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SupplyOrderResource {

    @Inject
    SupplyOrderService supplyOrderService;

    @POST
    public SupplyOrder createSupplyOrder() {
        return supplyOrderService.createDraft(currentFranchiseeId());
    }

    @POST
    @Path("/{orderId}/items")
    public SupplyOrderItem addItem(
            @PathParam("orderId") Long orderId,
            @Valid SupplyOrderItem item
    ) {
        return supplyOrderService.addItem(orderId, item);
    }

    @PATCH
    @Path("/{orderId}/items/{itemId}")
    public SupplyOrderItem updateItem(
            @PathParam("orderId") Long orderId,
            @PathParam("itemId") Long itemId,
            @Valid SupplyOrderItemPatch itemPatch
    ) {
        return supplyOrderService.patchItem(itemId, itemPatch);
    }

    @PATCH
    @Path("/{orderId}")
    public SupplyOrder updateSupplyOrder(
            @PathParam("orderId") Long orderId,
            @Valid SupplyOrderPatch patch
    ) {
        return supplyOrderService.patchOrder(orderId, patch);
    }

    private Long currentFranchiseeId() {
        return 1L;
    }
}
