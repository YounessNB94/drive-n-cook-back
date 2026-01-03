package fr.driv.n.cook.presentation.warehouse;

import fr.driv.n.cook.presentation.warehouse.dto.InventoryItem;
import fr.driv.n.cook.presentation.warehouse.dto.InventoryItemPatch;
import fr.driv.n.cook.presentation.warehouse.dto.Warehouse;
import fr.driv.n.cook.service.warehouse.InventoryService;
import fr.driv.n.cook.service.warehouse.WarehouseService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    public Response createInventoryItem(
            @PathParam("warehouseId") Long warehouseId,
            @Valid InventoryItem item
    ) {
        InventoryItem created = inventoryService.createInventoryItem(warehouseId, item);
        return Response.status(Response.Status.CREATED).entity(created).build();
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
}
