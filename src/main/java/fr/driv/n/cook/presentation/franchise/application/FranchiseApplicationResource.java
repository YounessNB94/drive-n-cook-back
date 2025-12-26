package fr.driv.n.cook.presentation.franchise.application;

import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplication;
import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplicationPatch;
import fr.driv.n.cook.presentation.franchise.application.dto.FranchiseApplicationRequest;
import fr.driv.n.cook.shared.FranchiseApplicationStatus;
import fr.driv.n.cook.shared.PaymentMethod;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;
import java.util.List;

@Path("/franchise-applications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FranchiseApplicationResource {

    @POST
    public FranchiseApplication submitApplication(@Valid FranchiseApplicationRequest request) {
        return stubApplication();
    }

    @GET
    public List<FranchiseApplication> listApplications(@QueryParam("franchisee") String franchisee) {
        return List.of(stubApplication(), stubApplication(2L));
    }

    @PATCH
    @Path("/{applicationId}")
    public FranchiseApplication updatePayment(
            @PathParam("applicationId") Long applicationId,
            @Valid FranchiseApplicationPatch patch
    ) {
        return stubApplication();
    }

    private FranchiseApplication stubApplication() {
        return stubApplication(1L);
    }

    private FranchiseApplication stubApplication(Long id) {
        return new FranchiseApplication(
                id,
                FranchiseApplicationStatus.PENDING,
                false,
                PaymentMethod.CARD,
                "PAY-REF",
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now()
        );
    }
}
