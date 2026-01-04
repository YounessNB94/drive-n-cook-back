package fr.driv.n.cook.presentation.loyalty.card;

import fr.driv.n.cook.presentation.loyalty.card.dto.LoyaltyCard;
import fr.driv.n.cook.presentation.loyalty.card.dto.LoyaltyCardLookup;
import fr.driv.n.cook.service.loyalty.card.LoyaltyCardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/loyalty-cards")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LoyaltyCardResource {

    @Inject
    LoyaltyCardService loyaltyCardService;

    @Inject
    JsonWebToken jsonWebToken;

    @POST
    public LoyaltyCard createLoyaltyCard() {
        return loyaltyCardService.createCard(currentFranchiseeId());
    }

    @GET
    public List<LoyaltyCard> listMyLoyaltyCards() {
        return loyaltyCardService.listForFranchisee(currentFranchiseeId());
    }

    @GET
    @Path("/{cardId}")
    public LoyaltyCard getLoyaltyCard(@PathParam("cardId") Long cardId) {
        return loyaltyCardService.getCardForFranchisee(cardId, currentFranchiseeId());
    }

    @GET
    @Path("/lookup")
    public LoyaltyCard lookupByCode(@Valid @BeanParam LoyaltyCardLookup lookup) {
        return loyaltyCardService.getByCustomerRef(lookup.code());
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
