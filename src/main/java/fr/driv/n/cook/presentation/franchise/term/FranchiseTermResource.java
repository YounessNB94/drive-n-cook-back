package fr.driv.n.cook.presentation.franchise.term;

import fr.driv.n.cook.presentation.franchise.term.dto.FranchiseTerm;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/franchise-terms")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FranchiseTermResource {

    @GET
    public FranchiseTerm getFranchiseTerms() {
        return new FranchiseTerm(
                "2025-01",
                "50000 EUR",
                "4% revenue",
                "80% from warehouses",
                "Full franchise terms..."
        );
    }
}
