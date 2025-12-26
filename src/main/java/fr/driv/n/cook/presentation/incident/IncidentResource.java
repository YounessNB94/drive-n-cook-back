package fr.driv.n.cook.presentation.incident;

import fr.driv.n.cook.presentation.incident.dto.Incident;
import fr.driv.n.cook.presentation.incident.dto.IncidentPatch;
import fr.driv.n.cook.presentation.shared.dto.IncidentStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;

@Path("/incidents")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class IncidentResource {

    @GET
    @Path("/{incidentId}")
    public Incident getIncident(@PathParam("incidentId") Long incidentId) {
        return stubIncident(incidentId);
    }

    @PATCH
    @Path("/{incidentId}")
    public Incident updateIncident(
            @PathParam("incidentId") Long incidentId,
            @Valid IncidentPatch patch
    ) {
        Incident incident = stubIncident(incidentId);
        String description = patch.description() != null ? patch.description() : incident.description();
        IncidentStatus status = patch.status() != null ? patch.status() : incident.status();
        return new Incident(
                incident.id(),
                incident.truckId(),
                description,
                status,
                incident.createdAt()
        );
    }

    private Incident stubIncident(Long id) {
        return new Incident(
                id,
                77L,
                "Engine check required",
                IncidentStatus.IN_PROGRESS,
                LocalDateTime.now().minusDays(1)
        );
    }
}
