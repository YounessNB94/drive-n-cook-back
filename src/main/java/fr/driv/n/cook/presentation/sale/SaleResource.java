package fr.driv.n.cook.presentation.sale;

import fr.driv.n.cook.presentation.sale.dto.Sale;
import fr.driv.n.cook.service.sale.SaleService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Path("/sales")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SaleResource {

    @Inject
    SaleService saleService;

    @Inject
    JsonWebToken jsonWebToken;
    
    @GET
    @Path("/me")
    public List<Sale> listMySales(
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to,
            @QueryParam("menuItemId") Long menuItemId
    ) {
        LocalDateTime dateFrom = from != null ? from.atStartOfDay() : null;
        LocalDateTime dateTo = to != null ? to.atTime(23, 59, 59) : null;
        return saleService.listSales(currentFranchiseeId(), dateFrom, dateTo, menuItemId);
    }

    @GET
    @RolesAllowed("ADMIN")
    public List<Sale> listSalesForAdmin(
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to,
            @QueryParam("menuItemId") Long menuItemId,
            @QueryParam("franchiseeId") Long franchiseeId
    ) {
        LocalDateTime dateFrom = from != null ? from.atStartOfDay() : null;
        LocalDateTime dateTo = to != null ? to.atTime(23, 59, 59) : null;
        return saleService.listSales(franchiseeId, dateFrom, dateTo, menuItemId);
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
