package fr.driv.n.cook.presentation.truck;

import fr.driv.n.cook.presentation.incident.dto.Incident;
import fr.driv.n.cook.presentation.truck.dto.MaintenanceRecord;
import fr.driv.n.cook.presentation.truck.dto.Truck;
import fr.driv.n.cook.presentation.truck.dto.TruckPatch;
import fr.driv.n.cook.service.incident.IncidentService;
import fr.driv.n.cook.service.truck.TruckService;
import fr.driv.n.cook.shared.TruckStatus;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.ResponseStatus;

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

    @Inject
    JsonWebToken jsonWebToken;

    @GET
    @Path("/me")
    public Truck getMyTruck() {
        return truckService.getTruck(currentFranchiseeId());
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

    @POST
    @Path("/{truckId}/maintenance-records")
    @RolesAllowed("ADMIN")
    @ResponseStatus(201)
    public MaintenanceRecord addMaintenanceRecord(
            @PathParam("truckId") Long truckId,
            @Valid MaintenanceRecord record
    ) {
        return truckService.addMaintenanceRecord(truckId, record);
    }

    @GET
    @Path("/{truckId}/maintenance-records")
    public List<MaintenanceRecord> listMaintenanceRecords(@PathParam("truckId") Long truckId) {
        return truckService.listMaintenance(truckId);
    }

    @POST
    @RolesAllowed("ADMIN")
    @ResponseStatus(201)
    public Truck createTruck(@Valid Truck truck) {
        return truckService.createTruck(truck);
    }

    @GET
    @RolesAllowed("ADMIN")
    public List<Truck> listAllTrucks(
            @QueryParam("status") TruckStatus status,
            @QueryParam("warehouseId") Long warehouseId
    ) {
        return truckService.listTrucks(status, warehouseId);
    }

    @GET
    @RolesAllowed("ADMIN")
    @Path("/{truckId}")
    public Truck getTruck(@PathParam("truckId") Long truckId) {
        return truckService.getTruck(truckId);
    }

    @PATCH
    @RolesAllowed("ADMIN")
    @Path("/{truckId}")
    public Truck patchTruck(
            @PathParam("truckId") Long truckId,
            @Valid TruckPatch patch
    ) {
        return truckService.patchTruck(truckId, patch);
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
