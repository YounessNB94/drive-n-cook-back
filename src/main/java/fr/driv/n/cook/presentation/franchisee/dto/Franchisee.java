package fr.driv.n.cook.presentation.franchisee.dto;

import fr.driv.n.cook.shared.FranchiseeRole;
import java.time.LocalDateTime;

public record Franchisee(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phone,
        String companyName,
        String address,
        LocalDateTime createdAt,
        FranchiseeRole role
) {
}
