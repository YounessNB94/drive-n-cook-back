package fr.driv.n.cook.presentation.loyalty.card;

import fr.driv.n.cook.presentation.loyalty.card.dto.LoyaltyCard;
import fr.driv.n.cook.presentation.loyalty.card.dto.LoyaltyCardLookup;
import fr.driv.n.cook.service.loyalty.card.LoyaltyCardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/loyalty-cards")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LoyaltyCardResource {

    @Inject
    LoyaltyCardService loyaltyCardService;

    @POST
    public LoyaltyCard createLoyaltyCard() {
        return loyaltyCardService.createCard();
    }

    @GET
    @Path("/{cardId}")
    public LoyaltyCard getLoyaltyCard(@PathParam("cardId") Long cardId) {
        return loyaltyCardService.getCard(cardId);
    }

    @GET
    public LoyaltyCard lookupByCode(@Valid @BeanParam LoyaltyCardLookup lookup) {
        return loyaltyCardService.getByCustomerRef(lookup.code());
    }
}
