package fr.driv.n.cook.presentation.franchisee.dto;

import jakarta.validation.constraints.Size;

public record FranchiseePatch(
        @Size(max = 100) String firstName,
        @Size(max = 100) String lastName,
        @Size(max = 30) String phone,
        @Size(max = 150) String companyName,
        @Size(max = 255) String address
) {
}
