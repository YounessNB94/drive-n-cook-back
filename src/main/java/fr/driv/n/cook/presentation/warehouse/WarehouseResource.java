package fr.driv.n.cook.presentation.warehouse;

import fr.driv.n.cook.presentation.warehouse.dto.InventoryItem;
import fr.driv.n.cook.presentation.warehouse.dto.Warehouse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.stream.Stream;

@Path("/warehouses")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class WarehouseResource {

    @GET
    public List<Warehouse> listWarehouses() {
        return List.of(
                new Warehouse(1L, "Paris Depot", "10 rue des Fleurs, Paris", "+3311111111"),
                new Warehouse(2L, "Lyon Hub", "5 avenue Lumière, Lyon", null)
        );
    }

    @GET
    @Path("/{warehouseId}/inventory-items")
    public List<InventoryItem> listInventoryItems(@PathParam("warehouseId") Long warehouseId) {
        return Stream.of(
                new InventoryItem(11L, warehouseId, "Tomato", "kg", 120),
                new InventoryItem(12L, warehouseId, "Tortilla", "piece", 450)
        ).toList();
    }
}
