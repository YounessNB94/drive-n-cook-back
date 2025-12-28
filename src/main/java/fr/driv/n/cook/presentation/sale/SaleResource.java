package fr.driv.n.cook.presentation.sale;

import fr.driv.n.cook.presentation.sale.dto.Sale;
import fr.driv.n.cook.service.sale.SaleService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Produces;

import java.time.LocalDate;
import java.util.List;

@Path("/sales")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SaleResource {

    @Inject
    SaleService saleService;

    @GET
    public List<Sale> listSales(
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to,
            @QueryParam("menuItemId") Long menuItemId
    ) {
        return saleService.listSales(rangeStart(from), rangeEnd(to), menuItemId);
    }

    private java.time.LocalDateTime rangeStart(LocalDate from) {
        return from != null ? from.atStartOfDay() : null;
    }

    private java.time.LocalDateTime rangeEnd(LocalDate to) {
        return to != null ? to.plusDays(1).atStartOfDay() : null;
    }
}
