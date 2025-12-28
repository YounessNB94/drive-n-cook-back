package fr.driv.n.cook.presentation.warehouse;

import fr.driv.n.cook.presentation.warehouse.dto.InventoryItem;
import fr.driv.n.cook.presentation.warehouse.dto.Warehouse;
import fr.driv.n.cook.service.warehouse.InventoryService;
import fr.driv.n.cook.service.warehouse.WarehouseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/warehouses")
@Produces(MediaType.APPLICATION_JSON)
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
}
