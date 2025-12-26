package fr.driv.n.cook.presentation.supply.order;

import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrder;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderItem;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderItemPatch;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderPatch;
import fr.driv.n.cook.shared.PaymentMethod;
import fr.driv.n.cook.shared.SupplyOrderStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.Objects;

@Path("/supply-orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SupplyOrderResource {

    private static final Logger LOGGER = Logger.getLogger(SupplyOrderResource.class);

    @POST
    public SupplyOrder createSupplyOrder(@Valid SupplyOrder supplyOrder) {
        Objects.requireNonNull(supplyOrder, "supplyOrder payload required");
        LOGGER.debugf("Creating supply order with payload: %s", supplyOrder);
        SupplyOrderStatus status = supplyOrder.status() != null ? supplyOrder.status() : SupplyOrderStatus.DRAFT;
        Long pickupWarehouseId = supplyOrder.pickupWarehouseId() != null ? supplyOrder.pickupWarehouseId() : 2L;
        boolean paid = supplyOrder.paid();
        PaymentMethod paymentMethod = supplyOrder.paymentMethod();
        String paymentRef = supplyOrder.paymentRef();
        return new SupplyOrder(
                1L,
                status,
                pickupWarehouseId,
                paid,
                paymentMethod,
                paymentRef,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @POST
    @Path("/{orderId}/items")
    public SupplyOrderItem addItem(
            @PathParam("orderId") Long orderId,
            @Valid SupplyOrderItem item
    ) {
        Objects.requireNonNull(item, "item payload required");
        LOGGER.debugf("Adding item %s to order %d", item, orderId);
        Long inventoryItemId = item.inventoryItemId();
        Integer quantity = item.quantity();
        return new SupplyOrderItem(5L, orderId, inventoryItemId, quantity);
    }

    @PATCH
    @Path("/{orderId}/items/{itemId}")
    public SupplyOrderItem updateItem(
            @PathParam("orderId") Long orderId,
            @PathParam("itemId") Long itemId,
            @Valid SupplyOrderItemPatch itemPatch
    ) {
        Objects.requireNonNull(itemPatch, "item patch payload required");
        LOGGER.debugf("Updating item %d on order %d with payload %s", itemId, orderId, itemPatch);
        Integer quantity = itemPatch.quantity();
        return new SupplyOrderItem(itemId, orderId, 42L, quantity);
    }

    @PATCH
    @Path("/{orderId}")
    public SupplyOrder updateSupplyOrder(
            @PathParam("orderId") Long orderId,
            @Valid SupplyOrderPatch patch
    ) {
        Objects.requireNonNull(patch, "order patch payload required");
        LOGGER.debugf("Updating order %d with patch %s", orderId, patch);
        SupplyOrder existing = stubOrder(orderId, SupplyOrderStatus.DRAFT);
        SupplyOrderStatus status = patch.status() != null ? patch.status() : existing.status();
        boolean paid = patch.paid() != null ? patch.paid() : existing.paid();
        PaymentMethod paymentMethod = patch.paymentMethod() != null ? patch.paymentMethod() : existing.paymentMethod();
        String paymentRef = patch.paymentRef() != null ? patch.paymentRef() : existing.paymentRef();
        Long pickupWarehouseId = patch.pickupWarehouseId() != null ? patch.pickupWarehouseId() : existing.pickupWarehouseId();
        return new SupplyOrder(
                existing.id(),
                status,
                pickupWarehouseId,
                paid,
                paymentMethod,
                paymentRef,
                existing.createdAt(),
                LocalDateTime.now()
        );
    }

    private SupplyOrder stubOrder(Long id, SupplyOrderStatus status) {
        return new SupplyOrder(
                id,
                status,
                2L,
                false,
                PaymentMethod.CARD,
                null,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusHours(12)
        );
    }
}
