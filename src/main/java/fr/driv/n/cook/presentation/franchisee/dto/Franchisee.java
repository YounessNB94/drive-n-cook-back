package fr.driv.n.cook.presentation.franchisee.dto;

import java.time.LocalDateTime;

public record Franchisee(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phone,
        String companyName,
        String address,
        LocalDateTime createdAt
) {
}
