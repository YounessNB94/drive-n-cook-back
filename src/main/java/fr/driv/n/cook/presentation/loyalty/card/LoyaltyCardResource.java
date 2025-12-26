package fr.driv.n.cook.presentation.loyalty.card;

import fr.driv.n.cook.presentation.loyalty.card.dto.LoyaltyCard;
import fr.driv.n.cook.presentation.loyalty.card.dto.LoyaltyCardLookup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;

@Path("/loyalty-cards")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LoyaltyCardResource {

    @POST
    public LoyaltyCard createLoyaltyCard(@Valid LoyaltyCard loyaltyCard) {
        return stubCard(1L, loyaltyCard.customerRef());
    }

    @GET
    @Path("/{cardId}")
    public LoyaltyCard getLoyaltyCard(@PathParam("cardId") Long cardId) {
        return stubCard(cardId, "customer-" + cardId);
    }

    @GET
    public LoyaltyCard lookupByCode(@Valid @BeanParam LoyaltyCardLookup lookup) {
        return stubCard(2L, lookup.code());
    }

    private LoyaltyCard stubCard(Long id, String customerRef) {
        String ref = customerRef != null ? customerRef : "customer-" + id;
        return new LoyaltyCard(id, ref, 1200, LocalDateTime.now().minusMonths(3));
    }
}
