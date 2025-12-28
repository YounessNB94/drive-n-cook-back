package fr.driv.n.cook.presentation.revenue;

import fr.driv.n.cook.presentation.revenue.dto.RevenuePoint;
import fr.driv.n.cook.service.revenue.RevenueService;
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
    public List<RevenuePoint> listRevenuePoints(
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to,
            @QueryParam("granularity") String granularity
    ) {
        return revenueService.listRevenuePoints(currentFranchiseeId(), from, to);
    }

    private Long currentFranchiseeId() {
        return 1L;
    }
}
