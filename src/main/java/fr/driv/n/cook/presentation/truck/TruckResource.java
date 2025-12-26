package fr.driv.n.cook.presentation.truck;

import fr.driv.n.cook.presentation.incident.dto.Incident;
import fr.driv.n.cook.shared.IncidentStatus;
import fr.driv.n.cook.shared.TruckStatus;
import fr.driv.n.cook.presentation.truck.dto.MaintenanceRecord;
import fr.driv.n.cook.presentation.truck.dto.Truck;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Path("/trucks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TruckResource {

    @GET
    @Path("/me")
    public Truck getMyTruck() {
        return new Truck(7L, "FR-123-AB", TruckStatus.ASSIGNED, 3L);
    }

    @POST
    @Path("/{truckId}/incidents")
    public Incident createIncident(
            @PathParam("truckId") Long truckId,
            @Valid Incident incident
    ) {
        Objects.requireNonNull(incident, "incident payload is required");
        IncidentStatus status = incident.status() != null ? incident.status() : IncidentStatus.OPEN;
        String description = incident.description();
        return new Incident(
                10L,
                truckId,
                description,
                status,
                LocalDateTime.now()
        );
    }

    @GET
    @Path("/{truckId}/incidents")
    public List<Incident> listIncidents(@PathParam("truckId") Long truckId) {
        return List.of(
                new Incident(10L, truckId, "Oil leakage", IncidentStatus.OPEN, LocalDateTime.now().minusDays(1)),
                new Incident(9L, truckId, "Brake check", IncidentStatus.RESOLVED, LocalDateTime.now().minusDays(5))
        );
    }

    @GET
    @Path("/{truckId}/maintenance-records")
    public List<MaintenanceRecord> listMaintenanceRecords(@PathParam("truckId") Long truckId) {
        return List.of(
                new MaintenanceRecord(1L, truckId, LocalDateTime.now().minusMonths(1), "Engine oil replacement"),
                new MaintenanceRecord(2L, truckId, LocalDateTime.now().minusMonths(2), "Tire rotation")
        );
    }
}
