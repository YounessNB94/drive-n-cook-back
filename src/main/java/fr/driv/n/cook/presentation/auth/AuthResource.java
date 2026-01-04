package fr.driv.n.cook.presentation.auth;

import fr.driv.n.cook.presentation.auth.dto.AuthTokenResponse;
import fr.driv.n.cook.presentation.auth.dto.FranchiseeLoginRequest;
import fr.driv.n.cook.presentation.franchisee.dto.Franchisee;
import fr.driv.n.cook.presentation.franchisee.dto.FranchiseeRegistration;
import fr.driv.n.cook.service.auth.AuthService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public AuthTokenResponse login(@Valid FranchiseeLoginRequest request) {
        return authService.login(request);
    }

    @POST
    @Path("/signup")
    public AuthTokenResponse signup(@Valid FranchiseeRegistration registration) {
        return authService.signup(registration);
    }

    @POST
    @Path("/promote/{franchiseeId}")
    @RolesAllowed("ADMIN")
    public Franchisee promoteFranchisee(@PathParam("franchiseeId") Long franchiseeId) {
        return authService.promoteFranchisee(franchiseeId);
    }
}
