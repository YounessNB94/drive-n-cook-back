package fr.driv.n.cook.presentation.franchise.application;

import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplication;
import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplicationPatch;
import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplicationRequest;
import fr.driv.n.cook.service.franchise.application.FranchiseApplicationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/franchise-applications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FranchiseApplicationResource {

    @Inject
    FranchiseApplicationService applicationService;

    @POST
    public FranchiseApplication submitApplication(@Valid FranchiseApplicationRequest request) {
        return applicationService.submit(currentFranchiseeId(), request);
    }

    @GET
    public List<FranchiseApplication> listApplications(@QueryParam("franchisee") String franchisee) {
        return applicationService.listForFranchisee(currentFranchiseeId());
    }

    @PATCH
    @Path("/{applicationId}")
    public FranchiseApplication updatePayment(
            @PathParam("applicationId") Long applicationId,
            @Valid FranchiseApplicationPatch patch
    ) {
        return applicationService.updatePayment(applicationId, patch);
    }

    private Long currentFranchiseeId() {
        return 1L;
    }
}
