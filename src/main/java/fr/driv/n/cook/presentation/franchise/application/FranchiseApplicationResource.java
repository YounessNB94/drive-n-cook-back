package fr.driv.n.cook.presentation.franchise.application;

import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplication;
import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplicationPatch;
import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplicationRequest;
import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplicationStatusPatch;
import fr.driv.n.cook.service.franchise.application.FranchiseApplicationService;
import fr.driv.n.cook.shared.FranchiseApplicationStatus;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/franchise-applications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FranchiseApplicationResource {

    @Inject
    FranchiseApplicationService applicationService;

    @Inject
    JsonWebToken jsonWebToken;

    @POST
    public FranchiseApplication submitApplication(@Valid FranchiseApplicationRequest request) {
        return applicationService.submit(currentFranchiseeId(), request);
    }

    @GET
    public List<FranchiseApplication> listApplications() {
        return applicationService.listForFranchisee(currentFranchiseeId());
    }

    @GET
    @Path("/admin")
    @RolesAllowed("ADMIN")
    public List<FranchiseApplication> listApplicationsForAdmin(
            @QueryParam("status") FranchiseApplicationStatus status
    ) {
        return applicationService.listAll(status);
    }

    @GET
    @Path("/{applicationId}")
    @RolesAllowed({"ADMIN"})
    public FranchiseApplication getApplication(
            @PathParam("applicationId") Long applicationId
    ) {
        return applicationService.getById(applicationId);
    }

    @PATCH
    @Path("/{applicationId}")
    public FranchiseApplication updatePayment(
            @PathParam("applicationId") Long applicationId,
            @Valid FranchiseApplicationPatch patch
    ) {
        return applicationService.updatePayment(applicationId, patch);
    }

    @PATCH
    @Path("/admin/{applicationId}/status")
    @RolesAllowed("ADMIN")
    public FranchiseApplication updateStatus(
            @PathParam("applicationId") Long applicationId,
            @Valid FranchiseApplicationStatusPatch patch
    ) {
        return applicationService.updateStatus(applicationId, patch.status());
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
