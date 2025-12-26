package fr.driv.n.cook.presentation.franchisee;

import fr.driv.n.cook.presentation.franchisee.dto.Franchisee;
import fr.driv.n.cook.presentation.franchisee.dto.FranchiseePatch;
import fr.driv.n.cook.presentation.franchisee.dto.FranchiseeRegistration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;

@Path("/franchisees")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FranchiseesResource {

    @GET
    @Path("/me")
    public Franchisee getOwnProfile() {
        return stubFranchisee();
    }

    @POST
    public Franchisee createFranchiseeAccount(@Valid FranchiseeRegistration registration) {
        return stubFranchisee();
    }

    @PATCH
    @Path("/me")
    public Franchisee updateOwnProfile(@Valid FranchiseePatch franchiseePatch) {
        return stubFranchisee();
    }

    private Franchisee stubFranchisee() {
        return new Franchisee(1L, "founder@drivncook.test", "Jane", "Doe", "+33123456789", "Driv'n Cook", "1 rue de Rivoli", LocalDateTime.now());
    }
}
