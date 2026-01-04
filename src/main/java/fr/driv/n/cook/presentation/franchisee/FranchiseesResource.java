package fr.driv.n.cook.presentation.franchisee;

import fr.driv.n.cook.presentation.franchisee.dto.Franchisee;
import fr.driv.n.cook.presentation.franchisee.dto.FranchiseePatch;
import fr.driv.n.cook.service.franchisee.FranchiseeService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

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

    @PATCH
    @Path("/me")
    public Franchisee updateOwnProfile(@Valid FranchiseePatch franchiseePatch) {
        return franchiseeService.updateProfile(currentFranchiseeId(), franchiseePatch);
    }

    @GET
    @RolesAllowed("ADMIN")
    public List<Franchisee> listFranchisees() {
        return franchiseeService.listAll();
    }

    @GET
    @Path("/{franchiseeId}")
    @RolesAllowed("ADMIN")
    public Franchisee getFranchiseeById(@PathParam("franchiseeId") Long franchiseeId) {
        return franchiseeService.getById(franchiseeId);
    }

    private Long currentFranchiseeId() {
        return 1L;
    }
}
