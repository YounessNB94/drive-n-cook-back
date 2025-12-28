package fr.driv.n.cook.presentation.incident;

import fr.driv.n.cook.presentation.incident.dto.Incident;
import fr.driv.n.cook.presentation.incident.dto.IncidentPatch;
import fr.driv.n.cook.service.incident.IncidentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/incidents")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class IncidentResource {

    @Inject
    IncidentService incidentService;

    @GET
    @Path("/{incidentId}")
    public Incident getIncident(@PathParam("incidentId") Long incidentId) {
        return incidentService.getIncident(incidentId);
    }

    @PATCH
    @Path("/{incidentId}")
    public Incident updateIncident(
            @PathParam("incidentId") Long incidentId,
            @Valid IncidentPatch patch
    ) {
        return incidentService.patchIncident(incidentId, patch);
    }
}
