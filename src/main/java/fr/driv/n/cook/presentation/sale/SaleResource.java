package fr.driv.n.cook.presentation.sale;

import fr.driv.n.cook.presentation.sale.dto.Sale;
import fr.driv.n.cook.shared.SaleChannel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Path("/sales")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SaleResource {

    @GET
    public List<Sale> listSales(
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to,
            @QueryParam("menuItemId") Long menuItemId
    ) {
        return List.of(
                new Sale(1L, LocalDateTime.now().minusDays(1), menuItemId, 12, new BigDecimal("180.50"), SaleChannel.ON_SITE),
                new Sale(2L, LocalDateTime.now().minusHours(3), 42L, 3, new BigDecimal("45.00"), SaleChannel.RESERVATION)
        );
    }
}
