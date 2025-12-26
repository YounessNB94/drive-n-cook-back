package fr.driv.n.cook.presentation.loyalty.card.dto;

import jakarta.validation.constraints.Size;
import jakarta.ws.rs.QueryParam;

public record LoyaltyCardLookup(
        @QueryParam("code")
        @Size(max = 100) String code
) {
}
