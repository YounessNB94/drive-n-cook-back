package fr.driv.n.cook.presentation.revenue;

import fr.driv.n.cook.presentation.revenue.dto.RevenuePoint;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Path("/revenues")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class RevenueResource {

    @GET
    public List<RevenuePoint> listRevenuePoints(
            @QueryParam("from") LocalDate from,
            @QueryParam("to") LocalDate to,
            @QueryParam("granularity") String granularity
    ) {
        String effectiveGranularity = granularity != null ? granularity.toLowerCase(Locale.ROOT) : "day";
        LocalDate start = from != null ? from : LocalDate.now().minusDays(7);
        LocalDate end = to != null ? to : LocalDate.now();
        int step = switch (effectiveGranularity) {
            case "week" -> 7;
            case "month" -> 30;
            default -> 1;
        };
        return List.of(
            new RevenuePoint(start, start.plusDays(step), new BigDecimal("950.00")),
            new RevenuePoint(end.minusDays(step), end, new BigDecimal("1020.40"))
        );
    }
}
