package fr.driv.n.cook.presentation.franchise.term;

import fr.driv.n.cook.presentation.franchise.term.dto.FranchiseTerm;
import fr.driv.n.cook.service.franchise.term.FranchiseTermService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/franchise-terms")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FranchiseTermResource {

    @Inject
    FranchiseTermService franchiseTermService;

    @GET
    public FranchiseTerm getFranchiseTerms() {
        return franchiseTermService.getLatest();
    }
}
