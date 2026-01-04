package fr.driv.n.cook.presentation.incident;

import fr.driv.n.cook.presentation.incident.dto.Incident;
import fr.driv.n.cook.presentation.incident.dto.IncidentPatch;
import fr.driv.n.cook.service.incident.IncidentService;
import fr.driv.n.cook.shared.IncidentStatus;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/incidents")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class IncidentResource {

    @Inject
    IncidentService incidentService;

    @Inject
    JsonWebToken jsonWebToken;

    @GET
    @RolesAllowed("ADMIN")
    public List<Incident> listIncidents(
            @QueryParam("status") IncidentStatus status,
            @QueryParam("truckId") Long truckId,
            @QueryParam("franchiseeId") Long franchiseeId
    ) {
        return incidentService.listIncidents(status, truckId, franchiseeId);
    }

    @GET
    @Path("/me")
    public List<Incident> listMyIncidents() {
        return incidentService.listIncidentsForFranchisee(currentFranchiseeId());
    }

    @GET
    @Path("/{incidentId}")
    public Incident getIncident(@PathParam("incidentId") Long incidentId) {
        if (isAdmin()) {
            return incidentService.getIncident(incidentId);
        }
        return incidentService.getIncidentForFranchisee(incidentId, currentFranchiseeId());
    }

    @PATCH
    @Path("/{incidentId}")
    @RolesAllowed("ADMIN")
    public Incident updateIncident(
            @PathParam("incidentId") Long incidentId,
            @Valid IncidentPatch patch
    ) {
        return incidentService.patchIncident(incidentId, patch);
    }

    @PATCH
    @Path("/{incidentId}/status")
    @RolesAllowed("ADMIN")
    public Incident updateIncidentStatus(
            @PathParam("incidentId") Long incidentId,
            @QueryParam("status") IncidentStatus status
    ) {
        if (status == null) {
            throw new BadRequestException("status requis");
        }
        return incidentService.markStatus(incidentId, status);
    }

    private boolean isAdmin() {
        return jsonWebToken != null && jsonWebToken.getGroups() != null && jsonWebToken.getGroups().contains("ADMIN");
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
