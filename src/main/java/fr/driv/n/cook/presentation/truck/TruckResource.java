package fr.driv.n.cook.presentation.truck;

import fr.driv.n.cook.presentation.incident.dto.Incident;
import fr.driv.n.cook.presentation.incident.dto.IncidentPatch;
import fr.driv.n.cook.presentation.truck.dto.MaintenanceRecord;
import fr.driv.n.cook.presentation.truck.dto.Truck;
import fr.driv.n.cook.service.incident.IncidentService;
import fr.driv.n.cook.service.truck.TruckService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/trucks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TruckResource {

    @Inject
    TruckService truckService;

    @Inject
    IncidentService incidentService;

    @GET
    @Path("/me")
    public Truck getMyTruck() {
        return truckService.getTruck(currentTruckId());
    }

    @POST
    @Path("/{truckId}/incidents")
    public Incident createIncident(
            @PathParam("truckId") Long truckId,
            @Valid Incident incident
    ) {
        return incidentService.createIncident(truckId, incident);
    }

    @GET
    @Path("/{truckId}/incidents")
    public List<Incident> listIncidents(@PathParam("truckId") Long truckId) {
        return truckService.listIncidents(truckId);
    }

    @GET
    @Path("/{truckId}/maintenance-records")
    public List<MaintenanceRecord> listMaintenanceRecords(@PathParam("truckId") Long truckId) {
        return truckService.listMaintenance(truckId);
    }

    private Long currentTruckId() {
        return 1L;
    }
}
