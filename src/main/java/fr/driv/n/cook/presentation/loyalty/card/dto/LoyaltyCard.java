package fr.driv.n.cook.presentation.loyalty.card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record LoyaltyCard(
        Long id,
        @Size(max = 100) String customerRef,
        Integer pointsBalance,
        LocalDateTime createdAt,
        Long franchiseeId
) {
}
