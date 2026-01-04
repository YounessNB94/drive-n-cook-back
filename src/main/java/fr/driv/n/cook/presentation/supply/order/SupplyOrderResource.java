package fr.driv.n.cook.presentation.supply.order;

import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrder;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderItem;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderItemPatch;
import fr.driv.n.cook.presentation.supply.order.dto.SupplyOrderPatch;
import fr.driv.n.cook.service.supply.order.SupplyOrderService;
import fr.driv.n.cook.shared.SupplyOrderStatus;
import io.quarkus.logging.Log;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/supply-orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SupplyOrderResource {

    @Inject
    SupplyOrderService supplyOrderService;

    @Inject
    JsonWebToken jsonWebToken;

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

    @GET
    public List<SupplyOrder> listSupplyOrders(
            @QueryParam("status") SupplyOrderStatus status,
            @QueryParam("warehouseId") Long warehouseId,
            @QueryParam("franchiseeId") Long franchiseeId,
            @QueryParam("paid") Boolean paid
    ) {
        if (isAdmin()) {
            return supplyOrderService.listOrdersForAdmin(status, warehouseId, franchiseeId, paid);
        }
        return supplyOrderService.listForFranchisee(currentFranchiseeId());
    }

    @GET
    @Path("/{orderId}")
    public SupplyOrder getSupplyOrder(@PathParam("orderId") Long orderId) {
        if (isAdmin()) {
            return supplyOrderService.getOrder(orderId);
        }
        return supplyOrderService.getOrderForFranchisee(orderId, currentFranchiseeId());
    }

    @GET
    @Path("/{orderId}/items")
    public List<SupplyOrderItem> listOrderItems(@PathParam("orderId") Long orderId) {
        if (!isAdmin()) {
            supplyOrderService.assertOrderOwnedByFranchisee(orderId, currentFranchiseeId());
        }
        return supplyOrderService.listItems(orderId);
    }

    @PATCH
    @Path("/{orderId}")
    public SupplyOrder updateSupplyOrder(
            @PathParam("orderId") Long orderId,
            @Valid SupplyOrderPatch patch
    ) {
        if (patch.status() == SupplyOrderStatus.READY) {
            enforceAdminRole();
            return supplyOrderService.markReady(orderId);
        }
        return supplyOrderService.patchOrder(orderId, patch);
    }

    private void enforceAdminRole() {
        if (!isAdmin()) {
            throw new ForbiddenException("Action réservée aux administrateurs");
        }
    }

    private boolean isAdmin() {
        return jsonWebToken != null && jsonWebToken.getGroups() != null && jsonWebToken.getGroups().contains("ADMIN");
    }

    private Long currentFranchiseeId() {
        String subject = jsonWebToken != null ? jsonWebToken.getSubject() : null;
        if (subject == null) {
            throw new NotAuthorizedException("Utilisateur non authentifié");
        }
        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException ex) {
            throw new NotAuthorizedException("Identifiant utilisateur invalide", ex);
        }
    }
}
