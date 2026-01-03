package fr.driv.n.cook.presentation.sale;

import fr.driv.n.cook.presentation.sale.dto.Sale;
import fr.driv.n.cook.service.sale.SaleService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Produces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Path("/sales")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SaleResource {

    @Inject
    SaleService saleService;

    @GET
    @Path("/me")
    public List<Sale> listMySales(
            @QueryParam("from") LocalDateTime from,
            @QueryParam("to") LocalDateTime to,
            @QueryParam("menuItemId") Long menuItemId
    ) {
        return saleService.listSales(currentFranchiseeId(), from, to, menuItemId);
    }

    @GET
    @RolesAllowed("ADMIN")
    public List<Sale> listSalesForAdmin(
            @QueryParam("from") LocalDateTime from,
            @QueryParam("to") LocalDateTime to,
            @QueryParam("menuItemId") Long menuItemId,
            @QueryParam("franchiseeId") Long franchiseeId
    ) {
        return saleService.listSales(franchiseeId, from, to, menuItemId);
    }

    private Long currentFranchiseeId() {
        return 1L;
    }
}
