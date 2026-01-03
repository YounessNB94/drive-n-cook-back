package fr.driv.n.cook.presentation.revenue;

import fr.driv.n.cook.presentation.revenue.dto.RevenuePoint;
import fr.driv.n.cook.service.revenue.RevenueService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Produces;

import java.time.LocalDate;
import java.util.List;

@Path("/revenues")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class RevenueResource {

    @Inject
    RevenueService revenueService;

     @GET
     @Path("/me")
     public List<RevenuePoint> listMyRevenuePoints(
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to
    ) {
        return revenueService.listRevenuePoints(currentFranchiseeId(), from, to);
    }

    @GET
    @RolesAllowed("ADMIN")
    public List<RevenuePoint> listRevenuePointsForAdmin(
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to,
            @QueryParam("franchiseeId") Long franchiseeId
    ) {
        return revenueService.listRevenuePoints(franchiseeId, from, to);
    }

    private Long currentFranchiseeId() {
        return 1L;
    }
}
