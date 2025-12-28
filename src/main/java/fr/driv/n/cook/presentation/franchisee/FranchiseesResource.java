package fr.driv.n.cook.presentation.franchisee;

import fr.driv.n.cook.presentation.franchisee.dto.Franchisee;
import fr.driv.n.cook.presentation.franchisee.dto.FranchiseePatch;
import fr.driv.n.cook.presentation.franchisee.dto.FranchiseeRegistration;
import fr.driv.n.cook.service.franchisee.FranchiseeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/franchisees")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FranchiseesResource {

    @Inject
    FranchiseeService franchiseeService;

    @GET
    @Path("/me")
    public Franchisee getOwnProfile() {
        return franchiseeService.getById(currentFranchiseeId());
    }

    @POST
    public Franchisee createFranchiseeAccount(@Valid FranchiseeRegistration registration) {
        return franchiseeService.register(registration);
    }

    @PATCH
    @Path("/me")
    public Franchisee updateOwnProfile(@Valid FranchiseePatch franchiseePatch) {
        return franchiseeService.updateProfile(currentFranchiseeId(), franchiseePatch);
    }

    private Long currentFranchiseeId() {
        return 1L;
    }
}
