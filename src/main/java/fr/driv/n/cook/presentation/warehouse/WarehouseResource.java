package fr.driv.n.cook.presentation.warehouse;

import fr.driv.n.cook.presentation.warehouse.dto.InventoryItem;
import fr.driv.n.cook.presentation.warehouse.dto.InventoryItemPatch;
import fr.driv.n.cook.presentation.warehouse.dto.Warehouse;
import fr.driv.n.cook.presentation.warehouse.dto.WarehouseAvailability;
import fr.driv.n.cook.service.supply.order.SupplyOrderService;
import fr.driv.n.cook.service.warehouse.InventoryService;
import fr.driv.n.cook.service.warehouse.WarehouseService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.util.List;

@Path("/warehouses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class WarehouseResource {

    @Inject
    WarehouseService warehouseService;

    @Inject
    InventoryService inventoryService;

    @Inject
    SupplyOrderService supplyOrderService;

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    JsonWebToken jsonWebToken;

    @GET
    public List<Warehouse> listWarehouses() {
        return warehouseService.listWarehouses();
    }

    @GET
    @Path("/{warehouseId}/inventory-items")
    public List<InventoryItem> listInventoryItems(@PathParam("warehouseId") Long warehouseId) {
        return inventoryService.listInventoryItems(warehouseId);
    }

    @POST
    @Path("/{warehouseId}/inventory-items")
    @RolesAllowed("ADMIN")
    @ResponseStatus(201)
    public InventoryItem createInventoryItem(
            @PathParam("warehouseId") Long warehouseId,
            @Valid InventoryItem item
    ) {
        return inventoryService.createInventoryItem(warehouseId, item);
    }

    @PATCH
    @Path("/{warehouseId}/inventory-items/{itemId}")
    @RolesAllowed("ADMIN")
    public InventoryItem patchInventoryItem(
            @PathParam("warehouseId") Long warehouseId,
            @PathParam("itemId") Long itemId,
            @Valid InventoryItemPatch patch
    ) {
        return inventoryService.patchInventoryItem(warehouseId, itemId, patch);
    }

    @GET
    @Path("/availability")
    public List<WarehouseAvailability> checkAvailability(
            @QueryParam("supplyOrderId") Long supplyOrderId
    ) {
        if (supplyOrderId == null) {
            throw new BadRequestException("supplyOrderId est requis");
        }
        if (!isAdmin()) {
            supplyOrderService.assertOrderOwnedByFranchisee(supplyOrderId, currentFranchiseeId());
        }
        return supplyOrderService.evaluateWarehouseAvailability(supplyOrderId);
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
